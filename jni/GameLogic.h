/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class GameLogic */

#ifndef _Included_GameLogic
#define _Included_GameLogic
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     GameLogic
 * Method:    manageHello
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_GameLogic_manageHello
  (JNIEnv *, jobject);

/*
 * Class:     GameLogic
 * Method:    managePickup
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_GameLogic_managePickup
  (JNIEnv *, jobject);

/*
 * Class:     GameLogic
 * Method:    manageLook
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_GameLogic_manageLook
  (JNIEnv *, jobject);

/*
 * Class:     GameLogic
 * Method:    manageMove
 * Signature: (C)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_GameLogic_manageMove
  (JNIEnv *, jobject, jchar);

/*
 * Class:     GameLogic
 * Method:    manageGetMap
 * Signature: ()LMap;
 */
JNIEXPORT jobject JNICALL Java_GameLogic_manageGetMap
  (JNIEnv *, jobject);

/*
 * Class:     GameLogic
 * Method:    manageSetMap
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_GameLogic_manageSetMap
  (JNIEnv *, jobject, jstring);

/*
 * Class:     GameLogic
 * Method:    manageSetPos
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_GameLogic_manageSetPos
  (JNIEnv *, jobject);

/*
 * Class:     GameLogic
 * Method:    manageSetPlayerNumber
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_GameLogic_manageSetPlayerNumber
  (JNIEnv *, jobject, jint, jint);

/*
 * Class:     GameLogic
 * Method:    managePrintMap
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_GameLogic_managePrintMap
  (JNIEnv *, jobject);

/*
 * Class:     GameLogic
 * Method:    manageInitiatePlayer
 * Signature: ()[I
 */
JNIEXPORT jintArray JNICALL Java_GameLogic_manageInitiatePlayer
  (JNIEnv *, jobject);

/*
 * Class:     GameLogic
 * Method:    manageCheckWin
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_GameLogic_manageCheckWin
  (JNIEnv *, jobject);

/*
 * Class:     GameLogic
 * Method:    manageQuitGame
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_GameLogic_manageQuitGame
  (JNIEnv *, jobject);

/*
 * Class:     GameLogic
 * Method:    manageGameRunning
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_GameLogic_manageGameRunning
  (JNIEnv *, jobject);

/*
 * Class:     GameLogic
 * Method:    manageEliminateFromGrid
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_GameLogic_manageEliminateFromGrid
  (JNIEnv *, jobject, jint);

/*
 * Class:     GameLogic
 * Method:    manageChangePosition
 * Signature: (IIC)[I
 */
JNIEXPORT jintArray JNICALL Java_GameLogic_manageChangePosition
  (JNIEnv *, jobject, jint, jint, jchar);

/*
 * Class:     GameLogic
 * Method:    manageDeletePosS
 * Signature: ([I)V
 */
JNIEXPORT void JNICALL Java_GameLogic_manageDeletePosS
  (JNIEnv *, jobject, jintArray);

/*
 * Class:     GameLogic
 * Method:    manageDeletePosN
 * Signature: ([I)V
 */
JNIEXPORT void JNICALL Java_GameLogic_manageDeletePosN
  (JNIEnv *, jobject, jintArray);

/*
 * Class:     GameLogic
 * Method:    manageDeletePosE
 * Signature: ([I)V
 */
JNIEXPORT void JNICALL Java_GameLogic_manageDeletePosE
  (JNIEnv *, jobject, jintArray);

/*
 * Class:     GameLogic
 * Method:    manageDeletePosW
 * Signature: ([I)V
 */
JNIEXPORT void JNICALL Java_GameLogic_manageDeletePosW
  (JNIEnv *, jobject, jintArray);

#ifdef __cplusplus
}
#endif
#endif
