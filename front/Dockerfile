# Use the official Node.js image as the base image
FROM node:18-alpine

WORKDIR /app

COPY package.json ./
COPY package-lock.json ./
RUN npm install

COPY . .

# Expose port 80 to access the app
EXPOSE 3000

# Start the app for development
CMD ["npm", "start"]