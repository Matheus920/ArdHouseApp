# ArdHouse

ArdHouse é um protótipo de automação residencial que integra uma interface gráfica a um Arduino. Por meio do aplicativo é possível controlar as ações de um Arduino, o qual realiza a ação desejada pelo usuário.

## 🏡Demonstração
### Splash screen (inicialização do aplicativo)
<img src="./app/src/main/res/splash.gif" alt="this slowpoke moves" width="150">

### Home, menu e indicadores de temperatura/umidade
<img src="./app/src/main/res/menu.gif" alt="this slowpoke moves" width="150">

### Selecionando o Arduino
<img src="./app/src/main/res/qrcode.gif" alt="this slowpoke moves" width="150">

### Acendendo a lâmpada
<img src="./app/src/main/res/lampada.gif" alt="this slowpoke moves" width="600">

### Alterando a velocidade do ventilador
<img src="./app/src/main/res/ventilador.gif" alt="this slowpoke moves" width="600">

### Ativando o alarme
<img src="./app/src/main/res/alarme.gif" alt="this slowpoke moves" width="380">

### Abrindo a porta
<img src="./app/src/main/res/porta.gif" alt="this slowpoke moves" width="600">


## 🚀 Tecnologias utilizadas
De antemão, ressalta-se que utilizamos o Android Studio (SDK 29) no projeto.

* [Java](https://www.java.com/pt-BR/) - Aplicativo
* [NodeJS](https://nodejs.org/en/) - Servidor (utilizou-se a framework express)
* [MongoDB](https://www.mongodb.com/) - Banco de dados
* [C++](https://www.arduino.cc/reference/pt/) - Arduino

## 📱 Instalação do aplicativo

Para simular o aplicativo em seu computador, utilizaremos o Android Studio versão maior que a 4.0.
* Clone o repositório no local desejado
```
git clone https://github.com/Matheus920/ArdHouseApp
```
* Abrir a pasta criada com o Android Studio
* Configurar o emulador ou conectar um dispositivo Android (verifique se a depuração está ativada no modo desenvolvedor)
* Rodar a aplicação

Obviamente, as funcionalidades do Arduino não funcionarão caso você não tenha a peça em mãos.

## ⚙ Configuração do Arduino
Com o aplicativo já instalado, agora podemos configurar o nosso Arduino para que ele mesmo realize as requisições dadas a ele.
* Clone o repositório no local desejado
```
git clone https://github.com/Matheus920/ArdHouseArduino
```
* Abrir o arquivo dentro da pasta com a [Arduino IDE](https://www.arduino.cc/en/software/)

* Por fim, monte o seguinte circuito utilizando o seu Arduino

![Circuito do Arduino](./app/src/main/res/esquema.BMP)

Na linha 25 de <b>arduino.ino</b> é definido o IP a ser utilizado pelo dispositivo
dentro da rede local. Para integrar com o aplicativo é necessário ler um QR Code que
corresponda ao IP do Arduino, de modo a possibilitar comunicação entre os dispositivos. O
valor padrão definido é de 192.168.15.75

## 📨 Servidor de envio de mensagens
O servidor está hospedado em https://ardhousenotifier.herokuapp.com, embora acessá-lo
diretamente irá resultar em erros devido às especificidades para as quais as requisições
estão programadas. Tanto o aplicativo quanto o arduino já estão programados para fazer as
requisições corretas, não é necessário se preocupar com isso.

Porém, para rodá-lo localmente, basta seguir os seguintes passos:
* Clone o repositório no local desejado
```
git clone https://github.com/Matheus920/ArdHouseNotifier
```
* Crie um arquivo <b>credentials.json</b> no diretório raiz e adicionar o seguinte conteúdo
disponível em https://notepad.pw/o136z2u7 com a senha “ArdHouse”
* Crie um banco de dados  MongoDB chamado test
* Finalmente, execute a seguinte linha de comando
```
node index.js
```

## 🤝 Como contribuir?

1. Faça o _fork_ do projeto
2. Crie uma _branch_ com sua modificação: (`git checkout -b feature/fooBar`)
3. Faça o _commit_: `git commit -am 'Add some fooBar'`
4. _Push_ a sua branch: `git push origin feature/fooBar`
5. Crie um novo _Pull Request_

## 💪 Contribuintes
* [**Vitor Martins**](github.com/vitormrts)
* [**Matheus Moreira**](github.com/Matheus920)
* [**Igor Augusto**](github.com/IgorSantoss)
* [**Gustavo Borsoi**](github.com/gborsoi)

## 📝Licença
Esse projeto está sob a licença MIT. Veja o arquivo LICENSE.md para mais detalhes.


