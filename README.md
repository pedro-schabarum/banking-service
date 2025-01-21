# banking-service

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/banking-service-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Related Guides

- REST ([guide](https://quarkus.io/guides/rest)): A Jakarta REST implementation utilizing build time processing and Vert.x. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it.
- REST Jackson ([guide](https://quarkus.io/guides/rest#json-serialisation)): Jackson serialization support for Quarkus REST. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it
- Hibernate ORM with Panache ([guide](https://quarkus.io/guides/hibernate-orm-panache)): Simplify your persistence code for Hibernate ORM via the active record or the repository pattern
- JDBC Driver - PostgreSQL ([guide](https://quarkus.io/guides/datasource)): Connect to the PostgreSQL database via JDBC
- Micrometer metrics ([guide](https://quarkus.io/guides/micrometer)): Instrument the runtime and your application with dimensional metrics using Micrometer.

## Provided Code

### Hibernate ORM

Create your first JPA entity

[Related guide section...](https://quarkus.io/guides/hibernate-orm)

[Related Hibernate with Panache section...](https://quarkus.io/guides/hibernate-orm-panache)


### REST

Easily start your REST Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)
<br><hr>

Para esta aula, será necessário subir um container do Prometheus que irá se integrar a nossa aplicação Quarkus com Micrometer. Siga os passos abaixo:


 ## Passo 1: Adicione a seguinte dependência no pom.xml 
 ```xml 
 <dependency> 
     <groupId>io.quarkus</groupId> 
     <artifactId>quarkus-micrometer-registry-prometheus</artifactId> 
 </dependency> 
 ``` 

 ## Passo 2: Habilite a exposição de métricas no application.properties 
 ```properties 
 quarkus.micrometer.export.prometheus.path=/metrics
 ``` 

 ## Passo 3: Crie o arquivo de configuração do Prometheus na raiz do projeto 
 ```yaml 
 scrape_configs: 
   - job_name: 'banking-service-app' 
     scrape_interval: 5s 
     metrics_path: '/metrics' 
     static_configs: 
       - targets: ['host.docker.internal:8080'] 
 ``` 

 ## Passo 4: Suba o container do Prometheus 
 Execute o comando abaixo para iniciar o Prometheus com o arquivo de configuração: 

 ### MacOS 
 ```bash 
 docker run -d --name prometheus \ 
   -p 9090:9090 \ 
   -v $(pwd)/prometheus.yml:/etc/prometheus/prometheus.yml \ 
   prom/prometheus 
 ``` 

 ### Windows 
 ```bash 
 docker run -d --name prometheus -p 9090:9090 -v C:/Users/PCESTUDIO/Workspace/banking-service/banking-service/prometheus.yaml:/etc/prometheus/prometheus.yml prom/prometheus 
 ``` 

 Obs: Lembrando que o nome PCESTUDIO é do computador que estou usando. No seu caso, você precisa alterar esse nome para o da sua máquina. 

 Agora, o Prometheus estará acessível em http://localhost:9090 e estará coletando métricas da aplicação Quarkus em execução localmente. 

 Nota: O endpoint host.docker.internal funciona para conexões entre o Docker e a máquina local no Windows e macOS. 

 ## Passo 5: Suba o container do Grafana 
 Com o container do Prometheus iniciado, execute o comando para iniciar o Grafana: 

 ```bash 
 docker run -d --name grafana -p 3000:3000 grafana/grafana 
 ``` 

 O Grafana estará acessível em http://localhost:3000 com as credenciais abaixo: 

 - Usuário padrão: admin 
 - Senha padrão: admin 

 Você será solicitado a redefinir a senha na primeira vez que fizer login. 

 ## Passo 6: Configure o Prometheus como fonte de dados no Grafana 
 1. Acesse o Grafana em http://localhost:3000. 
 2. Faça login com o usuário e senha padrão. 
 3. Vá para Configuration > Data Sources no menu lateral. 
 4. Clique em Add data source e selecione Prometheus. 
 5. Configure a URL do Prometheus: http://host.docker.internal:9090 (tanto no Windows quanto no macOS). 
 6. Clique em Save & Test. Se estiver configurado corretamente, o Grafana indicará que a conexão foi bem-sucedida. 
