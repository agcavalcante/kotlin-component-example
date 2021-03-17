### API com Kotlin, SpringBoot, Swagger, SpringSecurity, Jacoco, Prometheus e Grafana

Aqui esta descrito como fazer para iniciar a aplicação com todas as funcionalidades.

#### Getting Started

##### Utilizando o docker
A primeira coisa que deve saber é que existe a forma fácil e a forma dificil de fazer isso.

A forma **difícil** seria "subindo" todas as ferramentas na **"mão"** são elas:
* **Rabbit** - [Nosso message-broker.](https://www.rabbitmq.com/)  
* **Mongo** - [Nosso banco de dados NoSQL.](https://www.mongodb.com/) 
* **Prometheus** - [Nosso monitorador de eventos e alertas.](https://prometheus.io/)
* **Grafana** - [Nosso web-app que exibirá gráficos e por onde analisaremos os dados.](https://grafana.com/)

O que na teoria, é bom saber como se faz, mas desde que Solomon Hykes, Sebastien Pahl, Kamel Founadi criaram o **Docker**, as coisas melhoraram muito para nós desenvolvedores.

Então se olharem bem, na raíz do projeto temos um arquivo ```docker-compose.yml``` e dentro dele temos configurado todos os parâmetro para iniciar todas as aplicações citadas acima de maneira automática e rápida. Então caso você não tenha o **Docker** instalado, [faça isso antes de continuar](https://docs.docker.com/get-docker/).

###### Iniciando as aplicações do compose

A primeira coisa a ser feita é criar os containeres declarados no ```docker-compose.yml```, para isso basta utilizar o comando ```docker-compose up``` na raíz do projeto.

As aplicações irão subir nas seguintes portas:
* RabbitMQ - (http://localhost:15672) - a credencial padrão é **guest/guest**.
* MongoDB - (http://localhost:27017) - tenha um cliente de banco de dados, eu utilizo o [Robo3T](https://robomongo.org/download)
* Relay - (http://localhost:8090) - Nosso ponto de entrada da API.
* Consumer - (http://localhost:8091) - Que irá fazer a ponte entre o RabbitMQ e o MongoDB.
* Prometheus - (http://localhost:9090).
* Grafana - (http://localhost:3000) - a credencial padrão é **admin/admin**.

###### Veja bem:

O projeto por padrão, utiliza as imagens do docker que eu mesmo gerei e que estão no meu DOCKERHUB, veja nas linhas *14* e *28* do ```docker-compose.yml```.

Caso vocês queiram efetuar alguma alteração no projeto e precisem gerar uma nova imagem do consumer e do relay, comentem essas duas linhas e descomentem a *15* e *29*. Dentro da raíz do projeto, existe um arquivo - ```build.sh``` - shell não é meu forte mas criei para ajudar vocês a construirem as imagens do relay e do consumer de maneira automática, serão essas duas:
* spring/relay:latest
* spring/consumer:latest

![image](https://user-images.githubusercontent.com/36507028/111528305-c0119800-873f-11eb-8bfc-ebf7f6c4ab9a.png) ![image](https://user-images.githubusercontent.com/36507028/111528384-d881b280-873f-11eb-8461-7fa6dcd9dc14.png)

Mas lembrem-se, caso alterem alguma coisa e precisem gerar novas imagens, antes de executar o ```build.sh``` removam as imagens anteriormente geradas com o ```docker rmi [id_da_imagem]```.

Depois que executar o ```docker-compose up``` acesse o endpoint (http://localhost:9090/targets) e verifique se o status do endpoint **http://localhost:8090/actuator/prometheus** esta sinalizado como ***UP***.

![image](https://user-images.githubusercontent.com/36507028/111528804-475f0b80-8740-11eb-988d-3e9a3c8d6842.png)

Se não está assim, alguma coisa não deu certo na criação dos containers. *Faralemos disso daqui a pouco*.

Mas se está **UP** acesse a página *Graph* no menu do Prometheus e vamos utilizar nossa primeira query.

![image](https://user-images.githubusercontent.com/36507028/111529227-a7ee4880-8740-11eb-820c-02da9794d0ec.png)

###### Prometheus query example
* ```http_server_requests_seconds_count{instance="localhost:8090", status=~"2..", uri="/client"}```

Copie a query acima e de um **Execute**.

![image](https://user-images.githubusercontent.com/36507028/111529348-d1a76f80-8740-11eb-9844-9de916cdf04e.png)

Se tudo der certo, você não verá nenhum resultado. Para que isso ocorra precisamos criar eventos que serão *pegos* pelo Prometheus.

Utilizando o Postman com as collections disponibilizadas no projeto, gere seu cadastro de usuário com o endpoint **/signup**.

![image](https://user-images.githubusercontent.com/36507028/111529616-20550980-8741-11eb-9c03-92d33a1370f5.png)

Feito isso basta fazer o login com o endpoint, adivinhe só, **/login**. 

Agora com o endpoint **POST /client**, faça o primeiro cadastro de um cliente no banco e volte a tela do Prometheus. 

Realizando um novo **Execute**, teremos um dado gravado lá: 

![image](https://user-images.githubusercontent.com/36507028/111530038-9a858e00-8741-11eb-9aba-6502a035d853.png)

Foi o POST que acabamos de realizar. Se quiser ver as coisas mais bonitinhas, é só trocar a exibição para Graph: 

![image](https://user-images.githubusercontent.com/36507028/111530157-bab54d00-8741-11eb-9ba5-06a196194351.png)

###### Grafana query example
* ```http_server_requests_seconds_count{exception="None", instance="localhost:8090", job="spring-relay", status="200", uri="/client"}```

