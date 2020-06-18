#include "edu_illinois_library_poppler_PopplerPage.h"
#include <poppler-page.h>
#include "handle.h"

jstring Java_edu_illinois_library_poppler_PopplerPage_getLabel
(JNIEnv *env, jobject obj) {
    poppler::page *page = getHandle<poppler::page>(env, obj);
    poppler::ustring label = page->label();
    char *utfData = reinterpret_cast<char*>(label.to_utf8().data());
    return env->NewStringUTF(utfData);
}

void Java_edu_illinois_library_poppler_PopplerPage_nativeDestroy
(JNIEnv *env, jobject obj) {
    poppler::page *page = getHandle<poppler::page>(env, obj);
    setHandle<poppler::page>(env, obj, 0);
    delete page;
}

jint Java_edu_illinois_library_poppler_PopplerPage_orientation
(JNIEnv *env, jobject obj) {
    poppler::page *page = getHandle<poppler::page>(env, obj);
    switch (page->orientation()) {
        case poppler::page::orientation_enum::landscape:
            return 1;
        case poppler::page::orientation_enum::seascape:
            return 2;
        case poppler::page::orientation_enum::upside_down:
            return 3;
        default:
            return 0;
    }
}

jdoubleArray Java_edu_illinois_library_poppler_PopplerPage_pageRect
(JNIEnv *env, jobject obj) {
    poppler::page *page = getHandle<poppler::page>(env, obj);
    poppler::rectf pageRect = page->page_rect();

    double buf[] = { pageRect.width(), pageRect.height() };

    jdoubleArray jarr = env->NewDoubleArray(2);
    env->SetDoubleArrayRegion(jarr, 0, 2, buf);
    return jarr;
}
