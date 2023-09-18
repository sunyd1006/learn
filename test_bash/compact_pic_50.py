from PIL import Image
import os


from PIL import Image
import os

def compress_image(infile, outfile, targetSize = 50):
    im = Image.open(infile)  # 返回一个Image对象
    # os模块中的path目录下的getSize()方法获取文件大小，单位字节Byte
    size = os.path.getsize(infile)/1024  # 计算图片大小即KB
    print(size)
    # size的两个参数
    width, height = im.size[0], im.size[1]
    # 用于保存压缩过程中的temp路径,每次压缩会被不断覆盖
    while size > targetSize:
        width, height = round(width * 0.9), round(height * 0.9)
        print(width, height)
        im = im.resize((width, height), Image.ANTIALIAS)
        im.save(outfile)
        size = os.path.getsize(outfile)/1024

import fitz

'''
# 将PDF转化为图片
pdfPath pdf文件的路径
imgPath 图像要保存的文件夹
zoom_x x方向的缩放系数
zoom_y y方向的缩放系数
rotation_angle 旋转角度
'''
def pdf_image(pdfPath,imgPath,zoom_x,zoom_y,rotation_angle):
    # 打开PDF文件
    pdf = fitz.open(pdfPath)
    # 逐页读取PDF
    for pg in range(0, pdf.pageCount):
        page = pdf[pg]
        # 设置缩放和旋转系数
        trans = fitz.Matrix(zoom_x, zoom_y).preRotate(rotation_angle)
        pm = page.getPixmap(matrix=trans, alpha=False)
        # 开始写图像
        pm.writePNG(imgPath+str(pg)+".png")
    pdf.close()

def pdf_image2(pdfPath,imgPath,zoom_x,zoom_y,rotation_angle):
    # 打开PDF文件
    pdf = fitz.open(pdfPath)
    # 逐页读取PDF
    for pg in range(0, pdf.pageCount):
        page = pdf[pg]
        # 设置缩放和旋转系数
        trans = fitz.Matrix(zoom_x, zoom_y).preRotate(rotation_angle)
        pm = page.getPixmap(matrix=trans, alpha=False)
        # 开始写图像
        pm.writePNG(imgPath+str(pg)+".png")
    pdf.close()


def jpeg2jpg(path_in, path_out):
    img = Image.open(path_in)
    img.save(path_out, "JPEG", quality=80, optimize=True, progressive=True)


if __name__ == '__main__':
    # 压缩图片
    # compress_image(r'/Users/sunyindong/workspace/简历信息/各种证件/手照片/正面.jpg', '/Users/sunyindong/workspace/简历信息/各种证件/手照片/正面50.jpg')
    # compress_image(r'/Users/sunyindong/workspace/简历信息/各种证件/手照片/背面.jpg', '/Users/sunyindong/workspace/简历信息/各种证件/手照片/背面50.jpg')

    # pdf抽取图片
    # pdf_image("/Users/sunyindong/workspace/2022公务员与选调/海南选调报名表/孙大哥-推荐报名表.pdf", "/Users/sunyindong/workspace/2022公务员与选调/海南选调报名表",5,5,0)

    # jpeg to jpg
    # jpeg2jpg("/Users/sunyindong/workspace/孙大哥-偏黄-1寸-蓝底.jpeg", "/Users/sunyindong/workspace/孙大哥-偏黄-1寸-蓝底.jpg")
    compress_image(r'/Users/sunyindong/workspace/简历信息/个人照片/孙大哥-偏黄-1寸-蓝底.jpg', '/Users/sunyindong/workspace/简历信息/个人照片/孙大哥-偏黄-1寸-蓝底2.jpg', targetSize = 10)


