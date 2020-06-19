package br.usp.ardhouse.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import br.usp.R;
import br.usp.ardhouse.controller.SelecionarArduinoController;
import br.usp.ardhouse.infrastructure.ServerCallback;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.os.Vibrator;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

/*
    Essa classe é responsável pela tela de selecionar qual Arduino está ativo
    atualmente, estabelecendo a comunicação entre o dispositivo e o Arduino.
 */
public class SelecionarArduinoActivity extends AppCompatActivity {

    private SelecionarArduinoController controller;
    SurfaceView surfaceView;
    TextView txtBarcodeValue;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private Vibrator vibrador;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    String conteudoQRCode = "";


    // O método onCreate instancia os principais objetos que serão utilizados
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_selecionar_arduino);

        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);
        vibrador = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        controller = new SelecionarArduinoController(SelecionarArduinoActivity.this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (ActivityCompat.checkSelfPermission(SelecionarArduinoActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
        } else {
            finish();
        }
    }

    // O método serve para inicializar a leitura do QRCode, acessando a câmera do usuário
    private void initialiseDetectorsAndSources() {

        Toast.makeText(getApplicationContext(), "Iniciando leitura do QRCode", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) // IMPORTANTE
                .build();

        // Inicializa a câmera e pede permissão do usuário para utilizá-la
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(SelecionarArduinoActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(SelecionarArduinoActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                Toast.makeText(getApplicationContext(), "Leitura de QRCode parada", Toast.LENGTH_SHORT).show();
            }

            // Método para controlar as detecções de QRCode
            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {


                    txtBarcodeValue.post(new Runnable() {

                        @Override
                        public void run() {

                            // Se o QRCode for reconhecido, coloca na tela o conteúdo e vibra o celular
                            if (barcodes.valueAt(0) != null) {
                                txtBarcodeValue.removeCallbacks(null);
                                conteudoQRCode = barcodes.valueAt(0).rawValue;
                                txtBarcodeValue.setText(conteudoQRCode);
                                controller.salvarArduino(conteudoQRCode);
                                vibrador.vibrate(VibrationEffect.createOneShot(400, VibrationEffect.DEFAULT_AMPLITUDE));

                                controller.salvarNomeDispositivo(new ServerCallback() {
                                    @Override
                                    public void onSuccess(String result) {
                                        getSharedPreferences("_", Context.MODE_PRIVATE).edit().putString("deviceId", result).apply();
                                        if(result.matches("-?\\d+")) {
                                            Toast.makeText(SelecionarArduinoActivity.this, "Nome salvo com sucesso", Toast.LENGTH_SHORT).show();
                                            controller.salvarIdArduino(new ServerCallback() {
                                                @Override
                                                public void onSuccess(String result) {
                                                    Toast.makeText(SelecionarArduinoActivity.this, result, Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } else {
                                            Toast.makeText(SelecionarArduinoActivity.this, "Algo deu errado ao salvar o nome do dispositivo", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                                fecharAtividade();
                            }
                        }
                    });

                }
            }
        });
    }

    // Na saída da tela, para o detector de QRCode
    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    // Sempre que a tela de selecionar o Arduino é chamada, inicializa o detector de QRCode
    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }

    public void fecharAtividade(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
