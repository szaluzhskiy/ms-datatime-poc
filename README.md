1. docker run --rm   --name pg-docker -e POSTGRES_PASSWORD=docker -d -p 5432:5432 -v $HOME/docker/volumes/postgres:/var/lib/postgresql/data  postgres
2. Test REST DTO

  {
    "ld": "2020-04-01",
    "ldt": "2020-04-01T21:30:00.0",
    "odt": "2020-04-01T21:30:00.0+03:00",
    "odtz": "2020-04-01T21:30:00.0Z"
  }
  
#!Note
odtz - datetime provided in this field must be converted to UTC+0 on client side before sending. overwise   