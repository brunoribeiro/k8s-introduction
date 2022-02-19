FROM node:14.17.6-slim as build

WORKDIR /app
COPY . .
RUN npm install -g @angular/cli
RUN npm i
RUN ng build

FROM nginx:alpine
COPY --from=build /app/dist/weather-fe  /usr/share/nginx/html
CMD ["/bin/sh",  "-c",  "envsubst < /usr/share/nginx/html/assets/env-template.js > /usr/share/nginx/html/assets/env.js && exec nginx -g 'daemon off;'"]
