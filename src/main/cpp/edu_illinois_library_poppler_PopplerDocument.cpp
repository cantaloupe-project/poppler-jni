#include "edu_illinois_library_poppler_PopplerDocument.h"
#include <poppler-document.h>
#include "handle.h"
#include "util.h"

jlong Java_edu_illinois_library_poppler_PopplerDocument_loadFromData
(JNIEnv *env, jclass class_, jbyteArray jbytes) {
    poppler::byte_array *array = jByteArray2CharVector(env, jbytes);
    poppler::document *doc = poppler::document::load_from_data(array);
    delete array;
    return (jlong) doc;
}

jlong Java_edu_illinois_library_poppler_PopplerDocument_loadFromFile
(JNIEnv *env, jclass class_, jstring pathname) {
    std::string cpathname = jstring2string(env, pathname);
    poppler::document *doc = poppler::document::load_from_file(cpathname);
    return (jlong) doc;
}

jlong Java_edu_illinois_library_poppler_PopplerDocument_createPage
(JNIEnv *env, jobject obj, jint pageIndex) {
    poppler::document *doc = getHandle<poppler::document>(env, obj);
    return (jlong) doc->create_page(pageIndex);
}

jstring Java_edu_illinois_library_poppler_PopplerDocument_getAuthor
(JNIEnv *env, jobject obj) {
    poppler::document *doc = getHandle<poppler::document>(env, obj);
    poppler::ustring author = doc->get_author();
    char *utfData = reinterpret_cast<char*>(author.to_utf8().data());
    return env->NewStringUTF(utfData);
}

jint Java_edu_illinois_library_poppler_PopplerDocument_getNativeCreationDate
(JNIEnv *env, jobject obj) {
    poppler::document *doc = getHandle<poppler::document>(env, obj);
    return doc->get_creation_date();
}

jstring Java_edu_illinois_library_poppler_PopplerDocument_getCreator
(JNIEnv *env, jobject obj) {
    poppler::document *doc = getHandle<poppler::document>(env, obj);
    poppler::ustring creator = doc->get_creator();
    char *utfData = reinterpret_cast<char*>(creator.to_utf8().data());
    return env->NewStringUTF(utfData);
}

jstring Java_edu_illinois_library_poppler_PopplerDocument_getKeywords
(JNIEnv *env, jobject obj) {
    poppler::document *doc = getHandle<poppler::document>(env, obj);
    poppler::ustring keywords = doc->get_keywords();
    char *utfData = reinterpret_cast<char*>(keywords.to_utf8().data());
    return env->NewStringUTF(utfData);
}

jint Java_edu_illinois_library_poppler_PopplerDocument_getNativeModificationDate
(JNIEnv *env, jobject obj) {
    poppler::document *doc = getHandle<poppler::document>(env, obj);
    return doc->get_modification_date();
}

jstring Java_edu_illinois_library_poppler_PopplerDocument_getProducer
(JNIEnv *env, jobject obj) {
    poppler::document *doc = getHandle<poppler::document>(env, obj);
    poppler::ustring producer = doc->get_producer();
    char *utfData = reinterpret_cast<char*>(producer.to_utf8().data());
    return env->NewStringUTF(utfData);
}

jstring Java_edu_illinois_library_poppler_PopplerDocument_getSubject
(JNIEnv *env, jobject obj) {
    poppler::document *doc = getHandle<poppler::document>(env, obj);
    poppler::ustring subject = doc->get_subject();
    char *utfData = reinterpret_cast<char*>(subject.to_utf8().data());
    return env->NewStringUTF(utfData);
}

jstring Java_edu_illinois_library_poppler_PopplerDocument_getTitle
(JNIEnv *env, jobject obj) {
    poppler::document *doc = getHandle<poppler::document>(env, obj);
    poppler::ustring title = doc->get_title();
    char *utfData = reinterpret_cast<char*>(title.to_utf8().data());
    return env->NewStringUTF(utfData);
}

jboolean Java_edu_illinois_library_poppler_PopplerDocument_isEncrypted
(JNIEnv *env, jobject obj) {
    poppler::document *doc = getHandle<poppler::document>(env, obj);
    return doc->is_encrypted();
}

jboolean Java_edu_illinois_library_poppler_PopplerDocument_isLocked
(JNIEnv *env, jobject obj) {
    poppler::document *doc = getHandle<poppler::document>(env, obj);
    return doc->is_locked();
}

jstring Java_edu_illinois_library_poppler_PopplerDocument_metadata
(JNIEnv *env, jobject obj) {
    poppler::document *doc = getHandle<poppler::document>(env, obj);
    poppler::ustring metadata = doc->metadata();
    char *utfData = reinterpret_cast<char*>(metadata.to_utf8().data());
    return env->NewStringUTF(utfData);
}

void Java_edu_illinois_library_poppler_PopplerDocument_nativeDestroy
(JNIEnv *env, jobject obj) {
    poppler::document *doc = getHandle<poppler::document>(env, obj);
    setHandle<poppler::document>(env, obj, 0);
    delete doc;
}

jint Java_edu_illinois_library_poppler_PopplerDocument_numPages
(JNIEnv *env, jobject obj) {
    poppler::document *doc = getHandle<poppler::document>(env, obj);
    return doc->pages();
}
