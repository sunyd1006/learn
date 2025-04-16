struct ImgProp
{
	unsigned char HeaderInfo[54];
	unsigned long int Hbytes; // 水平字节数（向4位）取整数
	int Hpixels; // 水平像素

	int Vpixels; // 垂直像素
};

struct Pixel
{
	unsigned char R;
	unsigned char G;
	unsigned char B;
};

unsigned char** ReadBMP(char* );
void WriteBMP(unsigned char** , char*);

extern struct ImgProp 	ip;
