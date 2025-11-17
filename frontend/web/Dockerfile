FROM node:20-alpine

WORKDIR /app

# Copiar arquivos de dependências
COPY package*.json ./

# Instalar dependências
RUN npm install

# Copiar todo o código do projeto
COPY . .

# Expor a porta do Angular
EXPOSE 4200

# Comando para iniciar o servidor de desenvolvimento
CMD ["npm", "start", "--", "--host", "0.0.0.0"]
