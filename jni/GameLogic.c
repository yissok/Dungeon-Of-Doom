#include <jni.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <stdbool.h>
#include <time.h>
#include "GameLogic.h"

// headers
int doSomethingStrange(int x, int y);
char* itoaLib(int val, int base);


   
   
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
    * This function does nothing special
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

JNIEXPORT jstring JNICALL Java_GameLogic_manageHello(JNIEnv *env, jobject thisObj)
{
	char *result= "";//mapCoins-playerCoins;
	jclass thisClass = (*env)->GetObjectClass(env, thisObj);
	jfieldID fidMap = (*env)->GetFieldID(env, thisClass, "map", "LMap;");
	jobject joMap=(*env)->GetObjectField(env, thisObj, fidMap);

	jclass mapClass = (*env)->FindClass(env,"LMap;");
	jmethodID midMap = (*env)->GetMethodID(env, mapClass, "getWin", "()I");
	if (midMap == 0) return NULL;
	jint mapCoins;
	mapCoins = (*env)->CallIntMethod(env, joMap, midMap);

	result="GOLD: ";
	char *num;
	num=itoaLib(mapCoins,10);
	char *temp = malloc(strlen(result)*sizeof(char*)+strlen(num)*sizeof(char*)+1);
	strcpy(temp, result);
	result=strcat(temp, num);
	free(temp);

	return (*env)->NewStringUTF(env, result);
}

   
   
   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * This function does nothing special
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
JNIEXPORT jstring JNICALL Java_GameLogic_managePickup(JNIEnv *env, jobject thisObj)
{
	jclass thisClass = (*env)->GetObjectClass(env, thisObj);


	jfieldID fidIndex = (*env)->GetFieldID(env, thisClass, "playerNumber", "I");
	if (NULL == fidIndex) return NULL;
	jint number = (*env)->GetIntField(env, thisObj, fidIndex);

	jfieldID fidStandingOnGold = (*env)->GetFieldID(env, thisClass, "standingOnGold", "[Z");
	jbooleanArray SOGArray;
	SOGArray = (jbooleanArray)(*env)->GetObjectField(env,thisObj, fidStandingOnGold);
	jboolean *SOGArrayElements = (*env)->GetBooleanArrayElements(env,SOGArray, 0);
	bool stOnGold=SOGArrayElements[number];
	(*env)->ReleaseBooleanArrayElements(env,SOGArray, SOGArrayElements, 0);

	int goldCollected;
	char *result;
	result="FAIL\nThere is nothing to pick up...";
	if (stOnGold)
	{
		jfieldID fidStandingOnGold = (*env)->GetFieldID(env, thisClass, "standingOnGold", "[Z");
		jbooleanArray SOGArray;
		SOGArray = (jbooleanArray)(*env)->GetObjectField(env,thisObj, fidStandingOnGold);
		jboolean *SOGArrayElements = (*env)->GetBooleanArrayElements(env,SOGArray, 0);
		SOGArrayElements[number]=false;
		(*env)->ReleaseBooleanArrayElements(env,SOGArray, SOGArrayElements, 0);

		jfieldID fidCollectedGold = (*env)->GetFieldID(env, thisClass, "collectedGold", "[I");
		jintArray GCArray;
		GCArray = (jintArray)(*env)->GetObjectField(env,thisObj, fidCollectedGold);
		jint *GCArrayElements = (*env)->GetIntArrayElements(env,GCArray, 0);
		GCArrayElements[number]++;
		goldCollected=GCArrayElements[number];
		(*env)->ReleaseIntArrayElements(env,GCArray, GCArrayElements, 0);

		result="SUCCESS, GOLD COINS: ";
		char *num;
		num=itoaLib(goldCollected,10);
		char *temp = malloc(strlen(result)*sizeof(char*)+strlen(num)*sizeof(char*)+1*sizeof(char*));
		strcpy(temp, result);
		result=strcat(temp, num);
		free(temp);
	}


	//MAP OBJECT
	jfieldID fidMap22 = (*env)->GetFieldID(env, thisClass, "map", "LMap;");
	jobject joMap22=(*env)->GetObjectField(env, thisObj, fidMap22);
	jclass mapClass22 = (*env)->FindClass(env,"LMap;");


	jfieldID fidPlayerPosition22 = (*env)->GetFieldID(env, thisClass, "playerPosition", "[[I");
	jobject jobjectToPass = (*env)->GetObjectField(env,thisObj, fidPlayerPosition22);
	jobjectArray PPArray22 = (jobjectArray)(*env)->GetObjectField(env,thisObj, fidPlayerPosition22);
	jobject PPBiDim22 = (*env)->GetObjectArrayElement(env, PPArray22, number);
	jint *PPArrayElements22 = (*env)->GetIntArrayElements(env,PPBiDim22, 0);
	int posX22=0;
	int posY22=0;
	posX22=PPArrayElements22[0];//   <-----------------------------------------------------------------------posX
	posY22=PPArrayElements22[1];//   <-----------------------------------------------------------------------posY
	(*env)->ReleaseIntArrayElements(env,PPBiDim22, PPArrayElements22, 0);

	jmethodID midMap22 = (*env)->GetMethodID(env, mapClass22, "replaceTile", "(IIC)C");
	(*env)->CallCharMethod(env, joMap22, midMap22, posX22, posY22, 'P');

	jfieldID fidcollectedGold = (*env)->GetFieldID(env, thisClass, "collectedGold", "[I");
	jobject collectedGoldObj = (*env)->GetObjectField(env,thisObj, fidcollectedGold);

	jfieldID fidactive = (*env)->GetFieldID(env, thisClass, "active", "[Z");
	jobject activeObj = (*env)->GetObjectField(env,thisObj, fidactive);

	jfieldID fidstandingOnGold = (*env)->GetFieldID(env, thisClass, "standingOnGold", "[Z");
	jobject standingOnGoldObj = (*env)->GetObjectField(env,thisObj, fidstandingOnGold);

	jfieldID fidstandingOnExit = (*env)->GetFieldID(env, thisClass, "standingOnExit", "[Z");
	jobject standingOnExitObj = (*env)->GetObjectField(env,thisObj, fidstandingOnExit);

	jmethodID midMap33 = (*env)->GetMethodID(env, mapClass22, "updateTempDetails", "([[I[I[Z[Z[Z)V");
	(*env)->CallCharMethod(env, joMap22, midMap33, jobjectToPass, collectedGoldObj, activeObj, standingOnGoldObj, standingOnExitObj);


	if (stOnGold)
	{
		result="SUCCESS, GOLD COINS: ";
		char *num;
		num=itoaLib(goldCollected,10);
		char *temp = malloc(strlen(result)*sizeof(char*)+strlen(num)*sizeof(char*)+1*sizeof(char*));
		strcpy(temp, result);
		result=strcat(temp, num);
		free(temp);
	}
	else
	{
		result="FAIL\nThere is nothing to pick up...";
	}
	return (*env)->NewStringUTF(env, result);
}

   
   
   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * This function is interesting in teh last 15 lines
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
JNIEXPORT jstring JNICALL Java_GameLogic_manageLook(JNIEnv *env, jobject thisObj)
{
	jclass thisClass = (*env)->GetObjectClass(env, thisObj);


	jfieldID fidPlNum = (*env)->GetFieldID(env, thisClass, "playerNumber", "I");
	if (NULL == fidPlNum) return NULL;
	jint number = (*env)->GetIntField(env, thisObj, fidPlNum);
	//printf("Player Number: %d\n", number);

	jfieldID fidPlayerPosition = (*env)->GetFieldID(env, thisClass, "playerPosition", "[[I");
	jobjectArray PPArray = (jobjectArray)(*env)->GetObjectField(env,thisObj, fidPlayerPosition);
	jobject PPBiDim = (*env)->GetObjectArrayElement(env, PPArray, number);
	jint *PPArrayElements = (*env)->GetIntArrayElements(env,PPBiDim, 0);
	int posX=0;
	int posY=0;
	posX=PPArrayElements[0];
	posY=PPArrayElements[1];
	(*env)->ReleaseIntArrayElements(env,PPBiDim, PPArrayElements, 0);


	jfieldID fidMap = (*env)->GetFieldID(env, thisClass, "map", "LMap;");
	jobject joMap=(*env)->GetObjectField(env, thisObj, fidMap);

	jclass mapClass = (*env)->FindClass(env,"LMap;");
	jmethodID midMap = (*env)->GetMethodID(env, mapClass, "lookWindow", "(III)[[C");
	if (midMap == 0) return NULL;
	jobjectArray myMap;
	myMap = (*env)->CallObjectMethod(env, joMap, midMap, posX, posY, 5);



	char result[30]="OwwwOOwwwOOwwwOOwwwOOwwwO";
	result[29]='\0';
	jsize lenMain = (*env)->GetArrayLength(env, myMap);
	int i;

	for(i=0; i<lenMain; i++)
	{
		int j;
		for(j=0; j<lenMain; j++)
		{
			jobject BiDimBody = (*env)->GetObjectArrayElement(env, myMap, j);
			if (NULL == BiDimBody) return NULL;
			char *body = (char *)(*env)->GetCharArrayElements(env, BiDimBody, 0);
			if (NULL == body) return NULL;
			result[i*(lenMain)+j+i]=body[i*2];
		}
		result[(i+1)*(lenMain)+i]='\n';
	}
	//printf("result:\n%s\n", result);

	return (*env)->NewStringUTF(env, result);
}

   
   
   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * This function is interesting
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
JNIEXPORT jstring JNICALL Java_GameLogic_manageMove(JNIEnv *env, jobject thisObj, jchar direction)
{
	//printf("direction:%c\n", direction);



	jclass thisClass = (*env)->GetObjectClass(env, thisObj);

//PLAYER NUMBER
	jfieldID fidPlNum = (*env)->GetFieldID(env, thisClass, "playerNumber", "I");
	if (NULL == fidPlNum) return NULL;
	jint number = (*env)->GetIntField(env, thisObj, fidPlNum);//   <---------------------------------number
//STANDING ON GOLD
	jfieldID fidStandingOnGold = (*env)->GetFieldID(env, thisClass, "standingOnGold", "[Z");
	jbooleanArray SOGArray;
	SOGArray = (jbooleanArray)(*env)->GetObjectField(env,thisObj, fidStandingOnGold);
	jboolean *SOGArrayElements = (*env)->GetBooleanArrayElements(env,SOGArray, 0);
	bool stOnGold=SOGArrayElements[number];//   <------------------------------------------------------stOnGold
	(*env)->ReleaseBooleanArrayElements(env,SOGArray, SOGArrayElements, 0);
//STANDING ON EXIT
	jfieldID fidStandingOnExit = (*env)->GetFieldID(env, thisClass, "standingOnExit", "[Z");
	jbooleanArray SOEArray;
	SOEArray = (jbooleanArray)(*env)->GetObjectField(env,thisObj, fidStandingOnExit);
	jboolean *SOEArrayElements = (*env)->GetBooleanArrayElements(env,SOEArray, 0);
	bool stOnExit=SOEArrayElements[number];//   <------------------------------------------------------stOnExit
	(*env)->ReleaseBooleanArrayElements(env,SOEArray, SOEArrayElements, 0);
//POSITION
	jfieldID fidPlayerPosition = (*env)->GetFieldID(env, thisClass, "playerPosition", "[[I");
	jobjectArray PPArray = (jobjectArray)(*env)->GetObjectField(env,thisObj, fidPlayerPosition);
	jobject PPBiDim = (*env)->GetObjectArrayElement(env, PPArray, number);
	jint *PPArrayElements = (*env)->GetIntArrayElements(env,PPBiDim, 0);
	int posX=0;
	int posY=0;
	posX=PPArrayElements[0];//   <-----------------------------------------------------------------------posX
	posY=PPArrayElements[1];//   <-----------------------------------------------------------------------posY
	(*env)->ReleaseIntArrayElements(env,PPBiDim, PPArrayElements, 0);

	//printf("Player posx: %d posy: %d\n", posX,posY);
//MAP OBJECT
	jfieldID fidMap = (*env)->GetFieldID(env, thisClass, "map", "LMap;");
	jobject joMap=(*env)->GetObjectField(env, thisObj, fidMap);

	jclass mapClass = (*env)->FindClass(env,"LMap;");
	jmethodID midMap = (*env)->GetMethodID(env, mapClass, "replaceTile", "(IIC)C");
	if (midMap == 0) return NULL;
	(*env)->CallCharMethod(env, joMap, midMap, posX, posY, '.');


//POS OBJECT

	jmethodID midPos = (*env)->GetMethodID(env, thisClass, "changePosition", "(IIC)[I");
	if (midPos == 0) return NULL;
	jintArray myChangePos=(*env)->CallObjectMethod(env, thisObj, midPos, posX, posY, direction);


	int *newPPArrayElements = (int *)(*env)->GetIntArrayElements(env, myChangePos, 0);

	//jobject newPPBiDim = (*env)->GetObjectArrayElement(env, myChangePos, 0);
	//jint *newPPArrayElements = (*env)->GetIntArrayElements(env,myChangePos, 0);

	int newPosX=0;
	int newPosY=0;
	//////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////
	newPosX=newPPArrayElements[0];//   <-----------------------------------------------------------------------newPosX
	newPosY=newPPArrayElements[1];//   <-----------------------------------------------------------------------newPosY
	//////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////
	//printf("Player newPosX: %d newPosY: %d\n", newPosX,newPosY);



	if((newPosX==-1)&&(newPosY==-1))
	{
		jmethodID midMap2 = (*env)->GetMethodID(env, mapClass, "replaceTile", "(IIC)C");
		if (midMap2 == 0) return NULL;
		(*env)->CallCharMethod(env, joMap, midMap2, posX, posY, 'P');
		char *resp="FAIL";
		return (*env)->NewStringUTF(env, resp);
	}
	else
	{
		jobject PPBiDim2 = (*env)->GetObjectArrayElement(env, PPArray, number);
		jint *PPArrayElements2 = (*env)->GetIntArrayElements(env,PPBiDim2, 0);

		PPArrayElements2[0]=newPosX;//   <-----------------------------------------------------------------------posX
		PPArrayElements2[1]=newPosY;//   <-----------------------------------------------------------------------posY
		(*env)->ReleaseIntArrayElements(env,PPBiDim2, PPArrayElements2, 0);

		jmethodID midMap2 = (*env)->GetMethodID(env, mapClass, "replaceTile", "(IIC)C");
		if (midMap2 == 0) return NULL;
		(*env)->CallCharMethod(env, joMap, midMap2, newPosX, newPosY, 'P');

		jmethodID midCallBack = (*env)->GetMethodID(env, thisClass, "checkWin", "()Z");
		bool won=(*env)->CallBooleanMethod(env, thisObj, midCallBack);
		char *resp="SUCCESS";
		if(won)
		{
			jmethodID midCallBack2 = (*env)->GetMethodID(env, thisClass, "quitGame", "()Ljava/lang/String;");
			(*env)->CallObjectMethod(env, thisObj, midCallBack2);
		}
		return (*env)->NewStringUTF(env, resp);
	}




	//MAP OBJECT
		jfieldID fidMap22 = (*env)->GetFieldID(env, thisClass, "map", "LMap;");
		jobject joMap22=(*env)->GetObjectField(env, thisObj, fidMap22);
		jclass mapClass22 = (*env)->FindClass(env,"LMap;");


		jfieldID fidPlayerPosition22 = (*env)->GetFieldID(env, thisClass, "playerPosition", "[[I");
		jobject jobjectToPass = (*env)->GetObjectField(env,thisObj, fidPlayerPosition22);
		jobjectArray PPArray22 = (jobjectArray)(*env)->GetObjectField(env,thisObj, fidPlayerPosition22);
		jobject PPBiDim22 = (*env)->GetObjectArrayElement(env, PPArray22, number);
		jint *PPArrayElements22 = (*env)->GetIntArrayElements(env,PPBiDim22, 0);
		int posX22=0;
		int posY22=0;
		posX22=PPArrayElements22[0];//   <-----------------------------------------------------------------------posX
		posY22=PPArrayElements22[1];//   <-----------------------------------------------------------------------posY
		(*env)->ReleaseIntArrayElements(env,PPBiDim22, PPArrayElements22, 0);

		jmethodID midMap22 = (*env)->GetMethodID(env, mapClass22, "replaceTile", "(IIC)C");
		(*env)->CallCharMethod(env, joMap22, midMap22, posX22, posY22, 'P');

		jfieldID fidcollectedGold = (*env)->GetFieldID(env, thisClass, "collectedGold", "[I");
		jobject collectedGoldObj = (*env)->GetObjectField(env,thisObj, fidcollectedGold);

		jfieldID fidactive = (*env)->GetFieldID(env, thisClass, "active", "[Z");
		jobject activeObj = (*env)->GetObjectField(env,thisObj, fidactive);

		jfieldID fidstandingOnGold = (*env)->GetFieldID(env, thisClass, "standingOnGold", "[Z");
		jobject standingOnGoldObj = (*env)->GetObjectField(env,thisObj, fidstandingOnGold);

		jfieldID fidstandingOnExit = (*env)->GetFieldID(env, thisClass, "standingOnExit", "[Z");
		jobject standingOnExitObj = (*env)->GetObjectField(env,thisObj, fidstandingOnExit);

		jmethodID midMap33 = (*env)->GetMethodID(env, mapClass22, "updateTempDetails", "([[I[I[Z[Z[Z)V");
		(*env)->CallCharMethod(env, joMap22, midMap33, jobjectToPass, collectedGoldObj, activeObj, standingOnGoldObj, standingOnExitObj);



}

   
   
   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * This function is interesting
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
JNIEXPORT jobject JNICALL Java_GameLogic_manageGetMap(JNIEnv *env, jobject thisObj)
{
	jclass thisClass = (*env)->GetObjectClass(env, thisObj);

	jfieldID fidMap = (*env)->GetFieldID(env, thisClass, "map", "LMap;");
	jobject joMap=(*env)->GetObjectField(env, thisObj, fidMap);
	return joMap;
}

   
   
   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * This function is not interesting
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
JNIEXPORT void JNICALL Java_GameLogic_manageSetMap(JNIEnv *env, jobject thisObj, jstring file)
{
	jclass thisClass = (*env)->GetObjectClass(env, thisObj);
	jfieldID fidMap = (*env)->GetFieldID(env, thisClass, "map", "LMap;");
	jobject joMap=(*env)->GetObjectField(env, thisObj, fidMap);

	jclass mapClass = (*env)->FindClass(env,"LMap;");
	jmethodID midMap = (*env)->GetMethodID(env, mapClass, "readMap", "(Ljava/lang/String;)V");
	(*env)->CallVoidMethod(env, joMap, midMap, file);
}

   
   
   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * This function is not interesting
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
JNIEXPORT void JNICALL Java_GameLogic_manageSetPos(JNIEnv *env, jobject thisObj)
{
	jclass thisClass = (*env)->GetObjectClass(env, thisObj);

//PLAYER NUMBER
	jfieldID fidPlNum = (*env)->GetFieldID(env, thisClass, "playerNumber", "I");
	jint number = (*env)->GetIntField(env, thisObj, fidPlNum);//   <---------------------------------number

	jfieldID fidMaxFromLast = (*env)->GetFieldID(env, thisClass, "maxFromLastSession", "I");
	jint MaxFromLast = (*env)->GetIntField(env, thisObj, fidMaxFromLast);//   <---------------------------------number


	if(MaxFromLast<number)
	{
		jmethodID midInitPl = (*env)->GetMethodID(env, thisClass, "initiatePlayer", "()[I");
		jintArray myInitPl;//   <------------------------------------------------------------------------------myChangePos
		myInitPl = (*env)->CallObjectMethod(env, thisObj, midInitPl);

		int *InitPlArrayElements = (int *)(*env)->GetIntArrayElements(env, myInitPl, 0);



	//POSITION
		jfieldID fidPlayerPosition = (*env)->GetFieldID(env, thisClass, "playerPosition", "[[I");
		jobjectArray PPArray = (jobjectArray)(*env)->GetObjectField(env,thisObj, fidPlayerPosition);
		jobject PPBiDim = (*env)->GetObjectArrayElement(env, PPArray, number);
		jint *PPArrayElements = (*env)->GetIntArrayElements(env,PPBiDim, 0);
		int posX=0;
		int posY=0;
		PPArrayElements[0]=InitPlArrayElements[0];
		PPArrayElements[1]=InitPlArrayElements[1];
		posX=PPArrayElements[0];//   <-----------------------------------------------------------------------posX
		posY=PPArrayElements[1];//   <-----------------------------------------------------------------------posY
		(*env)->ReleaseIntArrayElements(env,PPBiDim, PPArrayElements, 0);

	//MAP OBJECT
		jfieldID fidMap = (*env)->GetFieldID(env, thisClass, "map", "LMap;");
		jobject joMap=(*env)->GetObjectField(env, thisObj, fidMap);

		jclass mapClass = (*env)->FindClass(env,"LMap;");
		jmethodID midMap = (*env)->GetMethodID(env, mapClass, "replaceTile", "(IIC)C");
		(*env)->CallCharMethod(env, joMap, midMap, posX, posY, 'P');

		jfieldID fidAct = (*env)->GetFieldID(env, thisClass, "active", "[Z");
		jbooleanArray ActArray;
		ActArray = (jbooleanArray)(*env)->GetObjectField(env,thisObj, fidAct);
		jboolean *ActArrayElements = (*env)->GetBooleanArrayElements(env,ActArray, 0);
		ActArrayElements[number]=false;//   <------------------------------------------------------ActArrayElements
		(*env)->ReleaseBooleanArrayElements(env,ActArray, ActArrayElements, 0);
	}




//MAP OBJECT
	jfieldID fidMap2 = (*env)->GetFieldID(env, thisClass, "map", "LMap;");
	jobject joMap2=(*env)->GetObjectField(env, thisObj, fidMap2);
	jclass mapClass2 = (*env)->FindClass(env,"LMap;");


	jfieldID fidPlayerPosition2 = (*env)->GetFieldID(env, thisClass, "playerPosition", "[[I");
	jobject jobjectToPass = (*env)->GetObjectField(env,thisObj, fidPlayerPosition2);
	jobjectArray PPArray2 = (jobjectArray)(*env)->GetObjectField(env,thisObj, fidPlayerPosition2);
	jobject PPBiDim2 = (*env)->GetObjectArrayElement(env, PPArray2, number);
	jint *PPArrayElements2 = (*env)->GetIntArrayElements(env,PPBiDim2, 0);
	int posX=0;
	int posY=0;
	posX=PPArrayElements2[0];//   <-----------------------------------------------------------------------posX
	posY=PPArrayElements2[1];//   <-----------------------------------------------------------------------posY
	(*env)->ReleaseIntArrayElements(env,PPBiDim2, PPArrayElements2, 0);

	jmethodID midMap2 = (*env)->GetMethodID(env, mapClass2, "replaceTile", "(IIC)C");
	(*env)->CallCharMethod(env, joMap2, midMap2, posX, posY, 'P');

	jfieldID fidcollectedGold = (*env)->GetFieldID(env, thisClass, "collectedGold", "[I");
	jobject collectedGoldObj = (*env)->GetObjectField(env,thisObj, fidcollectedGold);

	jfieldID fidactive = (*env)->GetFieldID(env, thisClass, "active", "[Z");
	jobject activeObj = (*env)->GetObjectField(env,thisObj, fidactive);

	jfieldID fidstandingOnGold = (*env)->GetFieldID(env, thisClass, "standingOnGold", "[Z");
	jobject standingOnGoldObj = (*env)->GetObjectField(env,thisObj, fidstandingOnGold);

	jfieldID fidstandingOnExit = (*env)->GetFieldID(env, thisClass, "standingOnExit", "[Z");
	jobject standingOnExitObj = (*env)->GetObjectField(env,thisObj, fidstandingOnExit);

	jmethodID midMap3 = (*env)->GetMethodID(env, mapClass2, "updateTempDetails", "([[I[I[Z[Z[Z)V");
	(*env)->CallCharMethod(env, joMap2, midMap3, jobjectToPass, collectedGoldObj, activeObj, standingOnGoldObj, standingOnExitObj);

	jmethodID midMap4 = (*env)->GetMethodID(env, mapClass2, "writeToTemp", "()V");
	(*env)->CallVoidMethod(env, joMap2, midMap4);
}

   
   
   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * This function is not interesting
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
JNIEXPORT void JNICALL Java_GameLogic_manageSetPlayerNumber(JNIEnv *env, jobject thisObj, jint n, jint max)
{
	jclass thisClass = (*env)->GetObjectClass(env, thisObj);
	jfieldID fidPlNum = (*env)->GetFieldID(env, thisClass, "playerNumber", "I");
	jint number = (*env)->GetIntField(env, thisObj, fidPlNum);//   <---------------------------------number
	number=n;
	(*env)->SetIntField(env, thisObj, fidPlNum, number);
}

   
   
   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * This function is not interesting
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
JNIEXPORT void JNICALL Java_GameLogic_managePrintMap(JNIEnv *env, jobject thisObj)
{
	jclass thisClass = (*env)->GetObjectClass(env, thisObj);

	jfieldID fidMap = (*env)->GetFieldID(env, thisClass, "map", "LMap;");
	jobject joMap=(*env)->GetObjectField(env, thisObj, fidMap);

	jclass mapClass = (*env)->FindClass(env,"LMap;");
	jmethodID midMap = (*env)->GetMethodID(env, mapClass, "printMap", "()V");
	(*env)->CallVoidMethod(env, joMap, midMap);
}

   
   
   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * This function is more interesting
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
JNIEXPORT jintArray JNICALL Java_GameLogic_manageInitiatePlayer(JNIEnv *env, jobject thisObj)
{
	jintArray jRes=NULL;
	jclass thisClass = (*env)->GetObjectClass(env, thisObj);

	jfieldID fidMap = (*env)->GetFieldID(env, thisClass, "map", "LMap;");
	jobject joMap=(*env)->GetObjectField(env, thisObj, fidMap);

	jclass mapClass = (*env)->FindClass(env,"LMap;");
	jmethodID midMap = (*env)->GetMethodID(env, mapClass, "lookAtTile", "(II)C");
	if (midMap == 0) return NULL;
	char myMap;

	int randPosX=-2;
	int randPosY=-2;

	srand ( time(NULL) );//randomization

	do
	{
		randPosX=(rand() % 6)+1;
		randPosY=(rand() % 16)+1;
		myMap = (*env)->CallCharMethod(env, joMap, midMap, randPosX, randPosY);
	}
	while( (myMap=='#') || (myMap=='P') || (myMap=='G') || (myMap=='E'));
	jRes = (*env)->NewIntArray(env, 2);
	jint *res = (*env)->GetIntArrayElements(env,jRes, NULL);
	res[0]=randPosX;
	res[1]=randPosY;
	(*env)->ReleaseIntArrayElements(env,jRes, res, 0);
	return jRes;
}

   
   
   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * This function is not interesting
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
JNIEXPORT jboolean JNICALL Java_GameLogic_manageCheckWin(JNIEnv *env, jobject thisObj)
{
	jclass thisClass = (*env)->GetObjectClass(env, thisObj);

	jfieldID fidMap = (*env)->GetFieldID(env, thisClass, "map", "LMap;");
	jobject joMap=(*env)->GetObjectField(env, thisObj, fidMap);

	jclass mapClass = (*env)->FindClass(env,"LMap;");
	jmethodID midMap = (*env)->GetMethodID(env, mapClass, "getWin", "()I");
	int myWin = (*env)->CallIntMethod(env, joMap, midMap);//   <---------------------------------myWin
//PLAYER NUMBER
	jfieldID fidPlNum = (*env)->GetFieldID(env, thisClass, "playerNumber", "I");
	jint number = (*env)->GetIntField(env, thisObj, fidPlNum);//   <---------------------------------number
//STANDING ON GOLD
	jfieldID fidCollectedGold = (*env)->GetFieldID(env, thisClass, "collectedGold", "[I");
	jintArray GCArray;
	GCArray = (jintArray)(*env)->GetObjectField(env,thisObj, fidCollectedGold);
	jint *GCArrayElements = (*env)->GetIntArrayElements(env,GCArray, 0);
	int goldCollected=GCArrayElements[number];//   <------------------------------------------------------goldCollected
	(*env)->ReleaseIntArrayElements(env,GCArray, GCArrayElements, 0);
//STANDING ON EXIT
	jfieldID fidStandingOnExit = (*env)->GetFieldID(env, thisClass, "standingOnExit", "[Z");
	jbooleanArray SOEArray;
	SOEArray = (jbooleanArray)(*env)->GetObjectField(env,thisObj, fidStandingOnExit);
	jboolean *SOEArrayElements = (*env)->GetBooleanArrayElements(env,SOEArray, 0);
	bool stOnExit=SOEArrayElements[number];//   <------------------------------------------------------stOnExit
	(*env)->ReleaseBooleanArrayElements(env,SOEArray, SOEArrayElements, 0);

	if((goldCollected>=myWin)&&(stOnExit))
	{
		jmethodID midCallBack = (*env)->GetMethodID(env, thisClass, "eliminateFromGrid", "(I)V");
		(*env)->CallVoidMethod(env, thisObj, midCallBack, number);
		return true;
	}

	return false;
}

   
   
   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * This function is not interesting
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
JNIEXPORT jstring JNICALL Java_GameLogic_manageQuitGame(JNIEnv *env, jobject thisObj)
{
	jclass thisClass = (*env)->GetObjectClass(env, thisObj);
//PLAYER NUMBER
	jfieldID fidPlNum = (*env)->GetFieldID(env, thisClass, "playerNumber", "I");
	jint number = (*env)->GetIntField(env, thisObj, fidPlNum);//   <---------------------------------number

	jfieldID fidAct = (*env)->GetFieldID(env, thisClass, "active", "[Z");
	jbooleanArray ActArray;
	ActArray = (jbooleanArray)(*env)->GetObjectField(env,thisObj, fidAct);
	jboolean *ActArrayElements = (*env)->GetBooleanArrayElements(env,ActArray, 0);
	ActArrayElements[number]=false;//   <------------------------------------------------------ActArrayElements
	(*env)->ReleaseBooleanArrayElements(env,ActArray, ActArrayElements, 0);
	return (*env)->NewStringUTF(env, "The game will now exit");
}


   
   
   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * This function is not interesting
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
JNIEXPORT jboolean JNICALL Java_GameLogic_manageGameRunning(JNIEnv *env, jobject thisObj)
{
	jclass thisClass = (*env)->GetObjectClass(env, thisObj);
//PLAYER NUMBER
	jfieldID fidPlNum = (*env)->GetFieldID(env, thisClass, "playerNumber", "I");
	jint number = (*env)->GetIntField(env, thisObj, fidPlNum);//   <---------------------------------number

	jfieldID fidAct = (*env)->GetFieldID(env, thisClass, "active", "[Z");
	jbooleanArray ActArray;
	ActArray = (jbooleanArray)(*env)->GetObjectField(env,thisObj, fidAct);
	jboolean *ActArrayElements = (*env)->GetBooleanArrayElements(env,ActArray, 0);
	jboolean jb=ActArrayElements[number];//   <------------------------------------------------------ActArrayElements
	(*env)->ReleaseBooleanArrayElements(env,ActArray, ActArrayElements, 0);
	return jb;
}


   
   
   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * This function is not interesting
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
JNIEXPORT void JNICALL Java_GameLogic_manageEliminateFromGrid(JNIEnv *env, jobject thisObj, jint socketNumber)
{
	jclass thisClass = (*env)->GetObjectClass(env, thisObj);



//STANDING ON GOLD
	jfieldID fidStandingOnGold = (*env)->GetFieldID(env, thisClass, "standingOnGold", "[Z");
	jbooleanArray SOGArray;
	SOGArray = (jbooleanArray)(*env)->GetObjectField(env,thisObj, fidStandingOnGold);
	jboolean *SOGArrayElements = (*env)->GetBooleanArrayElements(env,SOGArray, 0);
	bool stOnGold=SOGArrayElements[socketNumber];//   <------------------------------------------------------stOnGold
	(*env)->ReleaseBooleanArrayElements(env,SOGArray, SOGArrayElements, 0);
//STANDING ON EXIT
	jfieldID fidStandingOnExit = (*env)->GetFieldID(env, thisClass, "standingOnExit", "[Z");
	jbooleanArray SOEArray;
	SOEArray = (jbooleanArray)(*env)->GetObjectField(env,thisObj, fidStandingOnExit);
	jboolean *SOEArrayElements = (*env)->GetBooleanArrayElements(env,SOEArray, 0);
	bool stOnExit=SOEArrayElements[socketNumber];//   <------------------------------------------------------stOnExit
	(*env)->ReleaseBooleanArrayElements(env,SOEArray, SOEArrayElements, 0);

	jfieldID fidPlayerPosition = (*env)->GetFieldID(env, thisClass, "playerPosition", "[[I");
	jobjectArray PPArray = (jobjectArray)(*env)->GetObjectField(env,thisObj, fidPlayerPosition);
	jobject PPBiDim = (*env)->GetObjectArrayElement(env, PPArray, socketNumber);
	jint *PPArrayElements = (*env)->GetIntArrayElements(env,PPBiDim, 0);
	int posX=0;
	int posY=0;
	posX=PPArrayElements[0];//   <------------------------------------------------------posX
	posY=PPArrayElements[1];//   <------------------------------------------------------posY
	(*env)->ReleaseIntArrayElements(env,PPBiDim, PPArrayElements, 0);

	jfieldID fidMap = (*env)->GetFieldID(env, thisClass, "map", "LMap;");
	jobject joMap=(*env)->GetObjectField(env, thisObj, fidMap);

	jclass mapClass = (*env)->FindClass(env,"LMap;");
	jmethodID midMap = (*env)->GetMethodID(env, mapClass, "replaceTile", "(IIC)C");



	if(stOnGold==true)
	{
		(*env)->CallCharMethod(env, joMap, midMap, posX, posY, 'G');
	}
	else
	{
		if(stOnExit==true)
		{
			(*env)->CallCharMethod(env, joMap, midMap, posX, posY, 'E');
		}
		else
		{
			(*env)->CallCharMethod(env, joMap, midMap, posX, posY, '.');
		}
	}


}













   
   
   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * This function is interesting as it manages the old position handler class
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
JNIEXPORT jintArray JNICALL Java_GameLogic_manageChangePosition(JNIEnv *env, jobject thisObj, jint posx, jint posy, jchar direction)
{
	int newPosX=posx;
	int newPosY=posy;
	switch (direction)
	{
		case 'N':
			newPosX -=1;
			break;
		case 'E':
			newPosY +=1;
			break;
		case 'S':
			newPosX +=1;
			break;
		case 'W':
			newPosY -=1;
			break;
		default:
			printf("incorrect direction\n");
			return NULL;
	}


	jclass thisClass = (*env)->GetObjectClass(env, thisObj);

	jfieldID fidMap = (*env)->GetFieldID(env, thisClass, "map", "LMap;");
	jobject joMap=(*env)->GetObjectField(env, thisObj, fidMap);

	jclass mapClass = (*env)->FindClass(env,"LMap;");
	jmethodID midMap = (*env)->GetMethodID(env, mapClass, "lookAtTile", "(II)C");
	if (midMap == 0) return NULL;
	char myMap = (*env)->CallCharMethod(env, joMap, midMap, newPosX, newPosY);

	if((myMap!='P')&&(myMap!='#'))
	{
		int a[]={newPosX,newPosY};
		jintArray myMapArr=(*env)->NewIntArray(env,2);
		(*env)->SetIntArrayRegion(env, myMapArr, 0, 2, a);

		jmethodID middeletePosN;
		jmethodID middeletePosS;
		jmethodID middeletePosE;
		jmethodID middeletePosW;

		switch (direction)
		{
			case 'N':middeletePosN = (*env)->GetMethodID(env, thisClass, "deletePosN", "([I)V");
					(*env)->CallVoidMethod(env, thisObj, middeletePosN,myMapArr);
				break;
			case 'E':middeletePosE = (*env)->GetMethodID(env, thisClass, "deletePosE", "([I)V");
					(*env)->CallVoidMethod(env, thisObj, middeletePosE,myMapArr);
				break;
			case 'S':middeletePosS = (*env)->GetMethodID(env, thisClass, "deletePosS", "([I)V");
					(*env)->CallVoidMethod(env, thisObj, middeletePosS,myMapArr);
				break;
			case 'W':middeletePosW = (*env)->GetMethodID(env, thisClass, "deletePosW", "([I)V");
					(*env)->CallVoidMethod(env, thisObj, middeletePosW,myMapArr);
				break;
			default:
				printf("incorrect direction\n");
				return NULL;
		}

		jfieldID fidIndex = (*env)->GetFieldID(env, thisClass, "playerNumber", "I");
		if (NULL == fidIndex) return NULL;
		jint number = (*env)->GetIntField(env, thisObj, fidIndex);


		jfieldID fidStandingOnGold = (*env)->GetFieldID(env, thisClass, "standingOnGold", "[Z");
		jbooleanArray SOGArray;
		SOGArray = (jbooleanArray)(*env)->GetObjectField(env,thisObj, fidStandingOnGold);
		jboolean *SOGArrayElements = (*env)->GetBooleanArrayElements(env,SOGArray, 0);

		if(myMap=='G')
		{
			SOGArrayElements[number]=true;
		}
		else
		{
			SOGArrayElements[number]=false;
		}
		(*env)->ReleaseBooleanArrayElements(env,SOGArray, SOGArrayElements, 0);


		jfieldID fidStandingOnExit = (*env)->GetFieldID(env, thisClass, "standingOnExit", "[Z");
		jbooleanArray SOEArray;
		SOEArray = (jbooleanArray)(*env)->GetObjectField(env,thisObj, fidStandingOnExit);
		jboolean *SOEArrayElements = (*env)->GetBooleanArrayElements(env,SOEArray, 0);
		if(myMap=='E')
		{
			SOEArrayElements[number]=true;
		}
		else
		{
			SOEArrayElements[number]=false;
		}
		(*env)->ReleaseBooleanArrayElements(env,SOEArray, SOEArrayElements, 0);


		return myMapArr;
	}
	else
	{
		int b[]={-1,-1};
		jintArray myMapArrb=(*env)->NewIntArray(env,2);
		(*env)->SetIntArrayRegion(env, myMapArrb, 0, 2, b);

		return myMapArrb;
	}


}
   
   
   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * This function is kind of standard
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
JNIEXPORT void JNICALL Java_GameLogic_manageDeletePosS(JNIEnv *env, jobject thisObj, jintArray pos)
{
	jclass thisClass = (*env)->GetObjectClass(env, thisObj);

	jfieldID fidIndex = (*env)->GetFieldID(env, thisClass, "playerNumber", "I");
	jint number = (*env)->GetIntField(env, thisObj, fidIndex);

//STANDING ON GOLD
	jfieldID fidStandingOnGold = (*env)->GetFieldID(env, thisClass, "standingOnGold", "[Z");
	jbooleanArray SOGArray;
	SOGArray = (jbooleanArray)(*env)->GetObjectField(env,thisObj, fidStandingOnGold);
	jboolean *SOGArrayElements = (*env)->GetBooleanArrayElements(env,SOGArray, 0);
	bool stOnGold=SOGArrayElements[number];//   <------------------------------------------------------stOnGold
	(*env)->ReleaseBooleanArrayElements(env,SOGArray, SOGArrayElements, 0);
//STANDING ON EXIT
	jfieldID fidStandingOnExit = (*env)->GetFieldID(env, thisClass, "standingOnExit", "[Z");
	jbooleanArray SOEArray;
	SOEArray = (jbooleanArray)(*env)->GetObjectField(env,thisObj, fidStandingOnExit);
	jboolean *SOEArrayElements = (*env)->GetBooleanArrayElements(env,SOEArray, 0);
	bool stOnExit=SOEArrayElements[number];//   <------------------------------------------------------stOnExit
	(*env)->ReleaseBooleanArrayElements(env,SOEArray, SOEArrayElements, 0);



	jfieldID fidMap = (*env)->GetFieldID(env, thisClass, "map", "LMap;");
	jobject joMap=(*env)->GetObjectField(env, thisObj, fidMap);

	jclass mapClass = (*env)->FindClass(env,"LMap;");
	jmethodID midMap = (*env)->GetMethodID(env, mapClass, "replaceTile", "(IIC)C");

	jintArray GCArray;
	jint *GCArrayElements = (*env)->GetIntArrayElements(env,pos, 0);

	if(stOnGold==true)
	{
		(*env)->CallCharMethod(env, joMap, midMap, GCArrayElements[0]-1, GCArrayElements[1], 'G');
	}
	else
	{
		if(stOnExit==true)
		{
			(*env)->CallCharMethod(env, joMap, midMap, GCArrayElements[0]-1, GCArrayElements[1], 'E');
		}
		else
		{
			(*env)->CallCharMethod(env, joMap, midMap, GCArrayElements[0]-1, GCArrayElements[1], '.');
		}
	}
}
   
   
   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * This function is kind of standard
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
JNIEXPORT void JNICALL Java_GameLogic_manageDeletePosN(JNIEnv *env, jobject thisObj, jintArray pos)
{
	jclass thisClass = (*env)->GetObjectClass(env, thisObj);

		jfieldID fidIndex = (*env)->GetFieldID(env, thisClass, "playerNumber", "I");
		jint number = (*env)->GetIntField(env, thisObj, fidIndex);

	//STANDING ON GOLD
		jfieldID fidStandingOnGold = (*env)->GetFieldID(env, thisClass, "standingOnGold", "[Z");
		jbooleanArray SOGArray;
		SOGArray = (jbooleanArray)(*env)->GetObjectField(env,thisObj, fidStandingOnGold);
		jboolean *SOGArrayElements = (*env)->GetBooleanArrayElements(env,SOGArray, 0);
		bool stOnGold=SOGArrayElements[number];//   <------------------------------------------------------stOnGold
		(*env)->ReleaseBooleanArrayElements(env,SOGArray, SOGArrayElements, 0);
	//STANDING ON EXIT
		jfieldID fidStandingOnExit = (*env)->GetFieldID(env, thisClass, "standingOnExit", "[Z");
		jbooleanArray SOEArray;
		SOEArray = (jbooleanArray)(*env)->GetObjectField(env,thisObj, fidStandingOnExit);
		jboolean *SOEArrayElements = (*env)->GetBooleanArrayElements(env,SOEArray, 0);
		bool stOnExit=SOEArrayElements[number];//   <------------------------------------------------------stOnExit
		(*env)->ReleaseBooleanArrayElements(env,SOEArray, SOEArrayElements, 0);



		jfieldID fidMap = (*env)->GetFieldID(env, thisClass, "map", "LMap;");
		jobject joMap=(*env)->GetObjectField(env, thisObj, fidMap);

		jclass mapClass = (*env)->FindClass(env,"LMap;");
		jmethodID midMap = (*env)->GetMethodID(env, mapClass, "replaceTile", "(IIC)C");

		jintArray GCArray;
		jint *GCArrayElements = (*env)->GetIntArrayElements(env,pos, 0);

		if(stOnGold==true)
		{
			(*env)->CallCharMethod(env, joMap, midMap, GCArrayElements[0]+1, GCArrayElements[1], 'G');
		}
		else
		{
			if(stOnExit==true)
			{
				(*env)->CallCharMethod(env, joMap, midMap, GCArrayElements[0]+1, GCArrayElements[1], 'E');
			}
			else
			{
				(*env)->CallCharMethod(env, joMap, midMap, GCArrayElements[0]+1, GCArrayElements[1], '.');
			}
		}
}
   
   
   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * This function is kind of standard
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
JNIEXPORT void JNICALL Java_GameLogic_manageDeletePosE(JNIEnv *env, jobject thisObj, jintArray pos)
{
	jclass thisClass = (*env)->GetObjectClass(env, thisObj);

	jfieldID fidIndex = (*env)->GetFieldID(env, thisClass, "playerNumber", "I");
	jint number = (*env)->GetIntField(env, thisObj, fidIndex);

//STANDING ON GOLD
	jfieldID fidStandingOnGold = (*env)->GetFieldID(env, thisClass, "standingOnGold", "[Z");
	jbooleanArray SOGArray;
	SOGArray = (jbooleanArray)(*env)->GetObjectField(env,thisObj, fidStandingOnGold);
	jboolean *SOGArrayElements = (*env)->GetBooleanArrayElements(env,SOGArray, 0);
	bool stOnGold=SOGArrayElements[number];//   <------------------------------------------------------stOnGold
	(*env)->ReleaseBooleanArrayElements(env,SOGArray, SOGArrayElements, 0);
//STANDING ON EXIT
	jfieldID fidStandingOnExit = (*env)->GetFieldID(env, thisClass, "standingOnExit", "[Z");
	jbooleanArray SOEArray;
	SOEArray = (jbooleanArray)(*env)->GetObjectField(env,thisObj, fidStandingOnExit);
	jboolean *SOEArrayElements = (*env)->GetBooleanArrayElements(env,SOEArray, 0);
	bool stOnExit=SOEArrayElements[number];//   <------------------------------------------------------stOnExit
	(*env)->ReleaseBooleanArrayElements(env,SOEArray, SOEArrayElements, 0);



	jfieldID fidMap = (*env)->GetFieldID(env, thisClass, "map", "LMap;");
	jobject joMap=(*env)->GetObjectField(env, thisObj, fidMap);

	jclass mapClass = (*env)->FindClass(env,"LMap;");
	jmethodID midMap = (*env)->GetMethodID(env, mapClass, "replaceTile", "(IIC)C");

	jintArray GCArray;
	jint *GCArrayElements = (*env)->GetIntArrayElements(env,pos, 0);

	if(stOnGold==true)
	{
		(*env)->CallCharMethod(env, joMap, midMap, GCArrayElements[0], GCArrayElements[1]-1, 'G');
	}
	else
	{
		if(stOnExit==true)
		{
			(*env)->CallCharMethod(env, joMap, midMap, GCArrayElements[0], GCArrayElements[1]-1, 'E');
		}
		else
		{
			(*env)->CallCharMethod(env, joMap, midMap, GCArrayElements[0], GCArrayElements[1]-1, '.');
		}
	}
}
   
   
   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * This function is kind of standard
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
JNIEXPORT void JNICALL Java_GameLogic_manageDeletePosW(JNIEnv *env, jobject thisObj, jintArray pos)
{
	jclass thisClass = (*env)->GetObjectClass(env, thisObj);

	jfieldID fidIndex = (*env)->GetFieldID(env, thisClass, "playerNumber", "I");
	jint number = (*env)->GetIntField(env, thisObj, fidIndex);

//STANDING ON GOLD
	jfieldID fidStandingOnGold = (*env)->GetFieldID(env, thisClass, "standingOnGold", "[Z");
	jbooleanArray SOGArray;
	SOGArray = (jbooleanArray)(*env)->GetObjectField(env,thisObj, fidStandingOnGold);
	jboolean *SOGArrayElements = (*env)->GetBooleanArrayElements(env,SOGArray, 0);
	bool stOnGold=SOGArrayElements[number];//   <------------------------------------------------------stOnGold
	(*env)->ReleaseBooleanArrayElements(env,SOGArray, SOGArrayElements, 0);
//STANDING ON EXIT
	jfieldID fidStandingOnExit = (*env)->GetFieldID(env, thisClass, "standingOnExit", "[Z");
	jbooleanArray SOEArray;
	SOEArray = (jbooleanArray)(*env)->GetObjectField(env,thisObj, fidStandingOnExit);
	jboolean *SOEArrayElements = (*env)->GetBooleanArrayElements(env,SOEArray, 0);
	bool stOnExit=SOEArrayElements[number];//   <------------------------------------------------------stOnExit
	(*env)->ReleaseBooleanArrayElements(env,SOEArray, SOEArrayElements, 0);



	jfieldID fidMap = (*env)->GetFieldID(env, thisClass, "map", "LMap;");
	jobject joMap=(*env)->GetObjectField(env, thisObj, fidMap);

	jclass mapClass = (*env)->FindClass(env,"LMap;");
	jmethodID midMap = (*env)->GetMethodID(env, mapClass, "replaceTile", "(IIC)C");

	jintArray GCArray;
	jint *GCArrayElements = (*env)->GetIntArrayElements(env,pos, 0);

	if(stOnGold==true)
	{
		(*env)->CallCharMethod(env, joMap, midMap, GCArrayElements[0], GCArrayElements[1]+1, 'G');
	}
	else
	{
		if(stOnExit==true)
		{
			(*env)->CallCharMethod(env, joMap, midMap, GCArrayElements[0], GCArrayElements[1]+1, 'E');
		}
		else
		{
			(*env)->CallCharMethod(env, joMap, midMap, GCArrayElements[0], GCArrayElements[1]+1, '.');
		}
	}
}


//http://www.strudel.org.uk/itoa/
//THIS IS A COPIED LIBRARY FUNCTION (IT SHOULD BE INCLUDED IN <stdlib.h> but it's not):
//http://www.strudel.org.uk/itoa/
char* itoaLib(int val, int base)
{
	static char buf[32] = {0};
	int i = 30;
	for(; val && i ; --i, val /= base)
	{
		buf[i] = "0123456789abcdef"[val % base];
	}
	return &buf[i+1];
}
//http://www.strudel.org.uk/itoa/
//THIS IS A COPIED LIBRARY FUNCTION (IT SHOULD BE INCLUDED IN <stdlib.h> but it's not):
//http://www.strudel.org.uk/itoa/


int doSomethingStrange(int x, int y){
	return (x*4)+(y-3);
}
//REALLY? IS IT NORMAL 1000? WHAT YOU TOLD US ABOUT THIS LAST PART OF THE COURSEWORK WAS ALL MISLEADING AND LED TO 1000 BLOODY LINES OF UNCOMPREHENSIBLE C CODE