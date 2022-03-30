export ACCESS_TOKEN=$(curl -X 'POST' \
                        'http://localhost:8080/resources/token' \
                        -H 'accept: application/json' \
                        -H 'Content-Type: application/json' \
                        -d '{
                        "username": "admin",
                        "senha": "shoto"
                      }' | jq -r '.access_token')

export ACCESS_HEADER="Authorization: Bearer $ACCESS_TOKEN"

curl -X 'GET' \
  'http://localhost:8080/resources/entidades' \
  -H '$ACCESS_HEADER' \
  -H 'accept: application/json'| jq

curl -X 'POST' \
    'http://localhost:8080/resources/entidades' \
    -H 'accept: application/json' \
    -H '$ACCESS_HEADER' \
    -H 'Content-Type: application/json' \
    -d '{
    "id": 11,
    "descricao": "sdfsdfsdfsd"
  }' | jq

curl -X 'GET' \
  'http://localhost:8080/resources/entidades' \
  -H '$ACCESS_HEADER' \
  -H 'accept: application/json'| jq

