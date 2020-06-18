#include "edu_illinois_library_poppler_PopplerPageRenderer.h"
#include <poppler-page-renderer.h>
#include "handle.h"
#include "util.h"

int popplerFormat2JavaFormat(poppler::image::format_enum format) {
    // N.B.: these must align with the values in the PopplerImage.Format enum.
    switch (format) {
        case poppler::image::format_enum::format_mono:
            return 1;
        case poppler::image::format_enum::format_rgb24:
            return 2;
        case poppler::image::format_enum::format_argb32:
            return 3;
        case poppler::image::format_enum::format_gray8:
            return 4;
        case poppler::image::format_enum::format_bgr24:
            return 5;
        default:
            return 0;
    }
}

poppler::image::format_enum javaFormat2PopplerFormat(int javaFormat) {
    poppler::image::format_enum format;
    switch (javaFormat) {
        case 1:
            format = poppler::image::format_enum::format_invalid;
            break;
        case 2:
            format = poppler::image::format_enum::format_mono;
            break;
        case 3:
            format = poppler::image::format_enum::format_rgb24;
            break;
        case 4:
            format = poppler::image::format_enum::format_argb32;
            break;
        case 5:
            format = poppler::image::format_enum::format_gray8;
            break;
        case 6:
            format = poppler::image::format_enum::format_bgr24;
            break;
    }
    return format;
}

jboolean Java_edu_illinois_library_poppler_PopplerPageRenderer_canRender
(JNIEnv *env, jobject obj) {
    poppler::page_renderer *renderer = getHandle<poppler::page_renderer>(env, obj);
    return renderer->can_render();
}

void Java_edu_illinois_library_poppler_PopplerPageRenderer_initialize
(JNIEnv *env, jobject obj) {
    poppler::page_renderer *renderer = new poppler::page_renderer();
    setHandle(env, obj, renderer);
}

void Java_edu_illinois_library_poppler_PopplerPageRenderer_nativeDestroy
(JNIEnv *env, jobject obj) {
    poppler::page_renderer *renderer = getHandle<poppler::page_renderer>(env, obj);
    setHandle<poppler::page_renderer>(env, obj, 0);
    delete renderer;
}

jint Java_edu_illinois_library_poppler_PopplerPageRenderer_nativeGetImageFormat
(JNIEnv *env, jobject obj) {
    poppler::page_renderer *renderer = getHandle<poppler::page_renderer>(env, obj);
    return popplerFormat2JavaFormat(renderer->image_format());
}

jobject Java_edu_illinois_library_poppler_PopplerPageRenderer_renderPage
(JNIEnv *env, jobject obj, jobject pageObj, jdouble xRes, jdouble yRes,
jint x, jint y, jint width, jint height) {
    poppler::page_renderer *renderer = getHandle<poppler::page_renderer>(env, obj);
    poppler::page *page              = getHandle<poppler::page>(env, pageObj);
    poppler::image image             = renderer->render_page(page, xRes, yRes, x, y, width, height);

    jclass jimageClass = env->FindClass("edu/illinois/library/poppler/PopplerImage");
    jmethodID mid      = env->GetMethodID(jimageClass, "<init>", "()V");
    jobject jimage     = env->NewObject(jimageClass, mid);

    const char *data = image.const_data();
    jbyteArray jdata = charArray2JByteArray(env, data, image.bytes_per_row() * image.height());

    jclass c = env->GetObjectClass(jimage);
    env->SetObjectField(jimage, env->GetFieldID(c, "data", "[B"), jdata);
    env->SetIntField(jimage, env->GetFieldID(c, "format", "I"), popplerFormat2JavaFormat(image.format()));
    env->SetIntField(jimage, env->GetFieldID(c, "width", "I"), image.width());
    env->SetIntField(jimage, env->GetFieldID(c, "height", "I"), image.height());
    env->SetIntField(jimage, env->GetFieldID(c, "bytesPerRow", "I"), image.bytes_per_row());

    return jimage;
}

void Java_edu_illinois_library_poppler_PopplerPageRenderer_nativeSetImageFormat
(JNIEnv *env, jobject obj, jint formatID) {
    poppler::image::format_enum format = javaFormat2PopplerFormat(formatID);
    poppler::page_renderer *renderer = getHandle<poppler::page_renderer>(env, obj);
    renderer->set_image_format(format);
}

void Java_edu_illinois_library_poppler_PopplerPageRenderer_nativeSetRenderHint
(JNIEnv *env, jobject obj, jint enumID) {
    poppler::page_renderer::render_hint hint;
    switch (enumID) {
        case 1:
            hint = poppler::page_renderer::render_hint::antialiasing;
            break;
        case 2:
            hint = poppler::page_renderer::render_hint::text_antialiasing;
            break;
        case 3:
            hint = poppler::page_renderer::render_hint::text_hinting;
            break;
    }
    poppler::page_renderer *renderer = getHandle<poppler::page_renderer>(env, obj);
    renderer->set_render_hint(hint);
}
