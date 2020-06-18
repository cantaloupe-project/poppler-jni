#ifndef _UTIL_H_INCLUDED_
#define _UTIL_H_INCLUDED_

static std::string jstring2string(JNIEnv *env, jstring jStr) {
    if (!jStr) {
        return "";
    }
    const jclass stringClass = env->GetObjectClass(jStr);
    const jmethodID getBytes = env->GetMethodID(stringClass, "getBytes", "(Ljava/lang/String;)[B");
    const jbyteArray stringJbytes = (jbyteArray) env->CallObjectMethod(jStr, getBytes, env->NewStringUTF("UTF-8"));

    size_t length = (size_t) env->GetArrayLength(stringJbytes);
    jbyte* pBytes = env->GetByteArrayElements(stringJbytes, NULL);

    std::string ret = std::string((char *)pBytes, length);
    env->ReleaseByteArrayElements(stringJbytes, pBytes, JNI_ABORT);
    env->DeleteLocalRef(stringJbytes);
    env->DeleteLocalRef(stringClass);
    return ret;
}

static jbyteArray charArray2JByteArray(JNIEnv *env, const char* buf, int len) {
    jbyteArray jarray = env->NewByteArray(len);
    env->SetByteArrayRegion(jarray, 0, len, reinterpret_cast<const jbyte*>(buf));
    return jarray;
}

static const char* jByteArray2CharArray(JNIEnv *env, jbyteArray jarray) {
    int len = env->GetArrayLength(jarray);
    char *buf = new char[len];
    env->GetByteArrayRegion(jarray, 0, len, reinterpret_cast<jbyte*>(buf));
    return buf;
}

static std::vector<char>* jByteArray2CharVector(JNIEnv *env, jbyteArray jarray) {
    int len = env->GetArrayLength(jarray);
    std::vector<char> *result = new std::vector<char>(len);
    result->reserve(len);
    env->GetByteArrayRegion(jarray, 0, len, reinterpret_cast<jbyte*>(result->data()));
    return result;
}

#endif
