#ifndef COMMON_CU_H
#define COMMON_CU_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <cuda.h>

// // Read a 24-bit/pixel BMP file into a 1D linear array.
// // Allocate memory to store the 1D image and return its pointer.
// uch *ReadBMPlin(char* fn)
// {
// 	static uch *Img;
// 	FILE* f = fopen(fn, "rb");
// 	if (f == NULL){	printf("\n\n%s NOT FOUND\n\n", fn);	exit(EXIT_FAILURE); }

// 	uch HeaderInfo[54];
// 	fread(HeaderInfo, sizeof(uch), 54, f); // read the 54-byte header
// 	// extract image height and width from header
// 	int width = *(int*)&HeaderInfo[18];			ip.Hpixels = width;
// 	int height = *(int*)&HeaderInfo[22];		ip.Vpixels = height;
// 	int RowBytes = (width * 3 + 3) & (~3);		ip.Hbytes = RowBytes;
// 	//save header for re-use
// 	memcpy(ip.HeaderInfo, HeaderInfo,54);
// 	printf("\n Input File name: %17s  (%u x %u)   File Size=%u", fn,
// 			ip.Hpixels, ip.Vpixels, IMAGESIZE);
// 	// allocate memory to store the main image (1 Dimensional array)
// 	Img  = (uch *)malloc(IMAGESIZE);
// 	if (Img == NULL) return Img;      // Cannot allocate memory
// 	// read the image from disk
// 	fread(Img, sizeof(uch), IMAGESIZE, f);
// 	fclose(f);
// 	return Img;
// }


#endif COMMON_CU_H

