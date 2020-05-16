#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include<unistd.h>

#include "Map.h"

int doesFileExist(const char *filename);



   
   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    *
   	* INTRODUCTION TO GameLogic.c AND Map.c
   	*
    * Instead of going through each function I will explain HERE how all the below methods
    * work. In some interesting parts of the c code where it's les obvious, I'll insert
    * some comments there too. However you already know what all these methods do at a
    * high level (just check in the gameLogic comments written for CW1 in the gameLogic
    * class). With these comments we will focus on the low level.
    * 
    * What is happening is that all the content of all the methods of GameLogic have been
    * moved here. They can interact with the java class either by changing fields using
    * their ID or by returning values. 
    * 
    * So what I was saying about comments before, there is not much to comment, the logic
    * of how everything works is trivial and already seen before.
    * 
    * The less trivial aspect was conversion between java and c datatypes. Particularly
    * arrays and arrays of arrays. Another non trivial aspect was to find the corresponding
    * c library functions to the ones used in java (e.g. using: strtok, itoa, strlen...).
    * 
    * 
    * 
    * 
    * 
    * 
    * 
    * 
    * 
    * 
    * 
    * 
    * 
    * 
    * 
    * 
    * 
    * 
    * 
    * 
    * 
    * 
    * 
    * 
    * 
    * 
    * 
    * 
    * 
    * 
    * 
    * 
    * 
    * 
    * 
    * 
    * 
    * 
    * 
    * 
    * 
    * @param 
    * 
    * 
    * @return nothing
    * 
    * 
* * */







   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * This function does all the work. It decides when to load the recovery or the example_map
    * handling the different cases and allowing any map size.
    * 
    * 
    * 
    * 
    * 
    * 
    * 
    * @param mapPath
    * path to the file in string format
    * 
    * 
    * 
* * */
JNIEXPORT void JNICALL Java_Map_manageReadMap(JNIEnv *env, jobject thisObj, jstring mapPath)
{
	const char *nativeString = (*env)->GetStringUTFChars(env, mapPath, 0);
	const char *tempMapPath="maps/tempMap.txt";
	if (strcmp(nativeString,tempMapPath) == 0)
	{
				char ch;
				char file_name[]="maps/tempMap.txt";
				FILE *fp;
				fp = fopen(file_name,"r");

				size_t pos = ftell(fp);  //stackoverflow REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE
				fseek(fp, 0, SEEK_END);  //stackoverflow REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE
				size_t tLen = ftell(fp); //stackoverflow REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE
				fseek(fp, pos, SEEK_SET);//stackoverflow REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE

				int length=0;
				length=(int)tLen;
				if( fp == NULL )
				{
					printf("Error while opening the file.\n");
				}

				char resultOfContent[length];
				memset(resultOfContent, 0, (length+1)*sizeof(char) );

				int ColumnCounter=0;
				int ColNumSet=0;
				int RowCounter=0;
				if(length>12)//enough space for a file where the name and win identifiers are used and the map is 1X1
				{
					int i=0;
					int headerCharCount=0;
					int lineHeaderCounter=0;
					bool checkingOverhead=1;
					bool mapReadSuccess=true;
					while( ( ch = fgetc(fp) ) != EOF )
					{
						{
							if(ch=='$')
							{
								break;
							}

								if(ch=='\n')
								{
									resultOfContent[i]='|';
									RowCounter++;
									if((ColumnCounter!=ColNumSet)&&(RowCounter>1))
									{
										mapReadSuccess=false;
										break;
									}
									ColNumSet=ColumnCounter;
									ColumnCounter=0;
								}
								else
								{
									ColumnCounter++;
									resultOfContent[i]=ch;
								}

							i++;
						}
					}
					if(mapReadSuccess)
					{
						//good
					}
					else
					{
						printf("map reading failed, at least one row has less elements than the others.\n\n(HINT:check if there is more than one empty line after the last row of the map)\n");
					}

				}
				else
				{
					printf("file structure too short:\n\n");
				}
				fclose(fp);

				int RowNumSet=RowCounter;
				jchar mapMatrix[RowNumSet][ColNumSet];
				int i;
				int j;
				for(i=0; i<RowNumSet; i++)
				{
					for(j=0; j<ColNumSet; j++)
					{
						mapMatrix[i][j]=resultOfContent[i*(ColNumSet)+j+i];
					}
				}

				jclass thisClass = (*env)->GetObjectClass(env, thisObj);
				jmethodID midMap = (*env)->GetMethodID(env, thisClass, "loadMap", "([[C)V");
				jclass charClass = (*env)->FindClass(env,"[C");
				jobjectArray myMap=(*env)->NewObjectArray(env,RowNumSet,charClass,0);


				for(i=0; i<RowNumSet; i++)
				{
					jcharArray cod = (*env)->NewCharArray(env,ColNumSet);
					jchar *b=mapMatrix[i];
					(*env)->SetCharArrayRegion(env, cod, 0, ColNumSet, b);
					(*env)->SetObjectArrayElement(env, myMap, i, cod);
					(*env)->DeleteLocalRef (env,cod);
				}
				(*env)->CallVoidMethod(env, thisObj, midMap, myMap);
	}
	else
	{
		char ch;
		char *file_name="maps/example_map.txt";
		int aaa;
		aaa=doesFileExist(nativeString);
		if(aaa==0)
		{
			file_name="maps/example_map.txt";
		}
		else
		{
			file_name=(char *)nativeString;
		}
		FILE *fp;
		fp = fopen(file_name,"r");

		size_t pos = ftell(fp);  //stackoverflow REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE
		fseek(fp, 0, SEEK_END);  //stackoverflow REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE
		size_t tLen = ftell(fp); //stackoverflow REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE
		fseek(fp, pos, SEEK_SET);//stackoverflow REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE REFERENCE

		int length=0;
		length=(int)tLen;
		if( fp == NULL )
		{
			printf("Error while opening the file.\n");
		}

		char resultOfHeader[length];
		memset(resultOfHeader, 0, (length+1)*sizeof(char) );
		char resultOfContent[length];
		memset(resultOfContent, 0, (length+1)*sizeof(char) );

		int ColumnCounter=0;
		int ColNumSet=0;
		int RowCounter=0;
		if(length>12)//enough space for a file where the name and win identifiers are used and the map is 1X1
		{
			resultOfHeader[length-1]='\0';
			int i=0;
			int headerCharCount=0;
			int lineHeaderCounter=0;
			bool checkingOverhead=1;
			bool mapReadSuccess=true;
			while( ( ch = fgetc(fp) ) != EOF )
			{
				if(checkingOverhead)
				{
					resultOfHeader[i]=ch;
					i++;
					if(ch=='\n')
					{
						lineHeaderCounter++;
						if(lineHeaderCounter==2)
						{
							checkingOverhead=0;
							headerCharCount=i;
							i=0;
						}
					}
				}
				else
				{
					if(headerCharCount+i==(length-1))
					{
						if(ch!='\n')
						{
							ColumnCounter++;
							if((ColumnCounter!=ColNumSet)&&(RowCounter>=1))
							{
								mapReadSuccess=false;
								break;
							}
							resultOfContent[i]=ch;
						}
					}
					else
					{
						if(ch=='\n')
						{
							resultOfContent[i]='|';
							RowCounter++;
							if((ColumnCounter!=ColNumSet)&&(RowCounter>1))
							{
								mapReadSuccess=false;
								break;
							}
							ColNumSet=ColumnCounter;
							ColumnCounter=0;
						}
						else
						{
							ColumnCounter++;
							resultOfContent[i]=ch;
						}
					}
					i++;
				}
			}
			if(mapReadSuccess)
			{
				//good
			}
			else
			{
				printf("map reading failed, at least one row has less elements than the others.\n\n(HINT:check if there is more than one empty line after the last row of the map)\n");
			}

		}
		else
		{
			printf("file structure too short:\n\n");
		}
		fclose(fp);
		jclass thisClass = (*env)->GetObjectClass(env, thisObj);

		char *charres=resultOfHeader;

		char * separator = "\n";
		char * all = strtok((char *)charres, separator);
		char * winnumber = strtok(NULL, "");

		char *c=all;
		jstring forJava = (*env)->NewStringUTF(env, c);
		jmethodID az = (*env)->GetMethodID(env, thisClass, "setName", "(Ljava/lang/String;)Z");
		(*env)->CallBooleanMethod(env, thisObj, az, forJava);

		c=winnumber;
		jstring forJavaz = (*env)->NewStringUTF(env, c);
		jmethodID azz = (*env)->GetMethodID(env, thisClass, "setWin", "(Ljava/lang/String;)Z");
		(*env)->CallBooleanMethod(env, thisObj, azz, forJavaz);

		int RowNumSet=RowCounter+1;
		jchar mapMatrix[RowNumSet][ColNumSet];
		int i;
		int j;
		for(i=0; i<RowNumSet; i++)
		{
			for(j=0; j<ColNumSet; j++)
			{
				mapMatrix[i][j]=resultOfContent[i*(ColNumSet)+j+i];
			}
		}
		jmethodID midMap = (*env)->GetMethodID(env, thisClass, "loadMap", "([[C)V");
		jclass charClass = (*env)->FindClass(env,"[C");
		jobjectArray myMap=(*env)->NewObjectArray(env,RowNumSet,charClass,0);


		for(i=0; i<RowNumSet; i++)
		{
			jcharArray cod = (*env)->NewCharArray(env,ColNumSet);
			jchar *b=mapMatrix[i];
			(*env)->SetCharArrayRegion(env, cod, 0, ColNumSet, b);
			(*env)->SetObjectArrayElement(env, myMap, i, cod);
			(*env)->DeleteLocalRef (env,cod);
		}
		(*env)->CallVoidMethod(env, thisObj, midMap, myMap);

		jmethodID setupTemp = (*env)->GetMethodID(env, thisClass, "setUpTemp", "()V");
		(*env)->CallVoidMethod(env, thisObj, setupTemp);
	}

	(*env)->ReleaseStringUTFChars(env, mapPath, nativeString);

}

   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * This function simply sets the map class map field
    * 
    * @param map
    * is a copy of the char array of the map
* * */
JNIEXPORT void JNICALL Java_Map_manageLoadMap(JNIEnv *env, jobject thisObj, jobjectArray map)
{
	jclass thisClass = (*env)->GetObjectClass(env, thisObj);
	jfieldID fidPlayerPosition = (*env)->GetFieldID(env, thisClass, "map", "[[C");
	jobject mapArray = (*env)->GetObjectField(env,thisObj, fidPlayerPosition);
	mapArray=map;
	(*env)->SetObjectField(env, thisObj, fidPlayerPosition, mapArray);
}

   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * This function uses the strtok function that splits the string
    * 
    * @param in
    * ninput of a line of string starting with "win"
* * */
JNIEXPORT jboolean JNICALL Java_Map_manageSetWin(JNIEnv *env, jobject thisObj, jstring in)
{
	const char *a = (*env)->GetStringUTFChars(env, in, 0);

	char * separator = " ";
	char * b = strtok((char *)a, separator);
	char * c = strtok(NULL, "");

	int result = atoi(c);

	jclass thisClass = (*env)->GetObjectClass(env, thisObj);
	jfieldID mapnamefid = (*env)->GetFieldID(env, thisClass, "totalGoldOnMap", "I");
	jint mapArray = (*env)->GetIntField(env,thisObj, mapnamefid);
	mapArray=result;
	(*env)->SetIntField(env, thisObj, mapnamefid, mapArray);

	return true;//didn't guard properly for wrong formatting because of time constraints
}

   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * This function uses the strtok function that splits the string
    * 
    * @param mapPath
    * input of a line of string starting with "name"
* * */
JNIEXPORT jboolean JNICALL Java_Map_manageSetName(JNIEnv *env, jobject thisObj, jstring in)
{
	const char *a = (*env)->GetStringUTFChars(env, in, 0);


	char * separator = " ";
	char * b = strtok((char *)a, separator);
	char * c = strtok(NULL, "");

	jstring forJava = (*env)->NewStringUTF(env, c);

	jclass thisClass = (*env)->GetObjectClass(env, thisObj);
	jfieldID mapnamefid = (*env)->GetFieldID(env, thisClass, "mapName", "Ljava/lang/String;");
	jobject mapArray = (*env)->GetObjectField(env,thisObj, mapnamefid);
	mapArray=forJava;
	(*env)->SetObjectField(env, thisObj, mapnamefid, mapArray);

	return true;//didn't guard properly for wrong formatting because of time constraints

}




//LIBRARY function not written by me: http://stackoverflow.com/questions/230062/whats-the-best-way-to-check-if-a-file-exists-in-c-cross-platform
//LIBRARY function not written by me: http://stackoverflow.com/questions/230062/whats-the-best-way-to-check-if-a-file-exists-in-c-cross-platform
int doesFileExist(const char *filename) {
	if( access( filename, F_OK ) != -1 ) {
	   return 1;
	} else {
	    return 0;
	}
}
//LIBRARY function not written by me: http://stackoverflow.com/questions/230062/whats-the-best-way-to-check-if-a-file-exists-in-c-cross-platform
//LIBRARY function not written by me: http://stackoverflow.com/questions/230062/whats-the-best-way-to-check-if-a-file-exists-in-c-cross-platform
