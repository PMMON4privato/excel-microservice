#!/usr/bin bash

 docker build -t excel-ms-img .
 docker run --name excel-ms-cont -p 9267:9267 -t excel-ms-img
