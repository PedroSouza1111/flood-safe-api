
# 🛡️ Flood Safe API – Conteinerização com Docker + MySQL

Este projeto faz parte da disciplina **DevOps Tools & Cloud Computing**. Apresenta uma API Java com autenticação JWT, CRUD completo e integração com banco de dados MySQL em container.

---

## 📋 Pré-requisitos (na VM Ubuntu / Azure)

Instale o Docker e configure corretamente:

```bash
sudo apt update
sudo apt install -y docker.io
sudo systemctl enable docker
sudo systemctl start docker
sudo usermod -aG docker $USER
exit
# Reconecte na VM
```

---

## 📁 Clonar o projeto (ATENÇÂO- CLONAR A BRANCH CERTA)

```bash
git clone --branch oracle-devops --single-branch https://github.com/PedroSouza1111/flood-safe-api.git
cd flood-safe-api
```

---

## 🧱 Iniciar o container do MySQL

```bash
docker run -d \
  --name mysql-floodsafe \
  -e MYSQL_ROOT_PASSWORD=root123 \
  -e MYSQL_DATABASE=floodsafe \
  -e MYSQL_USER=devuser \
  -e MYSQL_PASSWORD=devpass \
  -v floodsafe_data:/var/lib/mysql \
  -p 3306:3306 \
  mysql:8.0
```

---

## 🏗 Build da imagem da API

```bash
docker build -t flood-safe-api .
```

---

## 🚀 Executar o container da API

```bash
docker run -d \
  --name floodsafe-app \
  -p 8080:8080 \
  flood-safe-api
```
###SEMPRE USE DOCKER PS PARA VERIFICAR O STATUS DOS CONTAINERES

---

## 🌐 Testando a API via Swagger UI

1. Acesse o Swagger UI no navegador:  
   👉 [http://IP da sua vm:8080/swagger-ui/index.html]

2. Encontre o endpoint de login (ex: `/auth/login`) e clique em **"Try it out"**

3. Preencha com as credenciais de exemplo:
```json
{
  "email": "seu@email.com",
  "senha": "sua_senha"
}
```

4. Clique em **Execute**

5. Copie o token JWT retornado na resposta

---

## 🔐 Usando o JWT no Postman (torna o teste mais claro e facil de entender)

1. Crie uma nova requisição para `http://ip da vm:8080/api/sensor` (ou outro endpoint)

2. Vá até a aba **Headers**

3. Adicione a chave de autorização:

```
Key: Authorization
Value: Bearer+seu token
```

4. Agora você pode testar os endpoints protegidos com o token autenticado!

---

##TABELA EXEMPLO-COMUNIDADES

### CREATE - Criar comunidade

POST http://ip da vm:8080/auth/login
Content-Type: application/json

{
  "nomeComunidade": "areiao",
  "regiao": "oeste",
  "latitude": 0.6,
  "longitude": 0.2,
  "nivelRiscoHistorico": "MEDIO"
}
```

### READ - Listar todas as comunidades
### READ - Buscar comunidade por ID
```
GET http://ip da vm:8080/comunidades/1  (ou sem o id para listar todas)

```

### UPDATE - Atualizar comunidade
```
PUT http://ip da vm:8080/comunidades/1

Content-Type: application/json

{
  "nomeComunidade": "rocinha",
  "regiao": "leste",
  "latitude": 0.6,
  "longitude": 0.2,
  "nivelRiscoHistorico": "BAIXO"
}
```
```

### DELETE - Deletar comunidade
```
DELETE http://ip da vm:8080/comunidades/1

```


## 📽 Vídeo demonstrativo

Link: [Assista no Google Drive](https://drive.google.com/file/d/1c5eUYEwbCcpe8ZPj_0tnM0D_4BPn9tSO/view?usp=sharing)

---

## 👨‍💻 Autor
Pedro Souza
## Branch devops
Felipe Rosa Peres  

