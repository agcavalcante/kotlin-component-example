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
