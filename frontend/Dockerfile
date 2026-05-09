# Use an official Node runtime as a parent image
FROM node:20-alpine as build-stage

# Set the working directory
WORKDIR /app

# Copy package.json and package-lock.json
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application code
COPY . .

# Build the application
RUN npm run build

# Use Nginx to serve the static files
FROM nginx:stable-alpine as production-stage

# Copy the build output to the Nginx html directory
COPY --from=build-stage /app/dist /usr/share/nginx/html

# Copy a custom nginx config
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expose port 80
EXPOSE 80

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]
