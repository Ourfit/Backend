from fastapi import FastAPI

app = FastAPI()


@app.get("/")
def read_root():
    return {"Hello": "World"}


@app.get("/ping")
def ping():
    return "pong"


@app.get("/initialize_databse")
def initialize_databse():
    from database import create_db_and_tables

    create_db_and_tables()
    return "Database initialized"
