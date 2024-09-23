# viaCepJson

A aplicação responsável pelo consumo das APIs do viaCepJson, com foco na consulta de Código de Endereçamento Postal (CEP) do Brasil.

### Pré Requisitos:

- [JDK 17](https://www.oracle.com/br/java/technologies/downloads/#java17)
- [Maven](https://maven.apache.org/download.cgi)

### Instalação:

Para instalar o projeto e suas dependências, execute o seguinte comando:

`mvn clean install`

### Executando o projeto:

- Abra o projeto na IDE de sua preferência;
- Verifique se o projeto foi importado e se todas as dependências do Maven foram resolvidas;
- Para compilar o projeto, execute:

`mvn clean package`

- Após a finalização do build, execute o comando:

`mvn spring-boot:run`

### Exemplos de Request

- Para consultar um determinado CEP, utilize o método GET para o endpoint /consulta-cep/01001000

GET http://localhost:8080/api/consulta-cep/01001000

- Para listar todos os CEPs consultados para uma determinada UF, utilize o método GET para o endpoint /lista-ceps:

GET http://localhost:8080/api/lista-ceps?uf=SP

### Execução de testes

- Para executar os testes unitários, utilize o comando:

`mvn test`