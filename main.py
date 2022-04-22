from fastapi import FastAPI, File, UploadFile
app = FastAPI()

@app.post("/uploadfile/")
async def create_upload_file(file: UploadFile):
    if file.filename.split(".")[-1] in ["jpg", "jpeg", "png","jfif"]:
        return {"filename": file.filename}
    return {"error": "invalid image"}




# from fastapi import FastAPI,File, UploadFile
# app = FastAPI()
# @app.post("/predict-image/")
# async def predict_api(file: UploadFile = File(...)):
#     # extension = file.filename.split(".")[-1] in ("jpg", "jpeg", "png","jfif")
#     # if not extension:
#     #     return "Image must be of proper format!"
#     # contents = await file.read()
#
#     return file.filename
