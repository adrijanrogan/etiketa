#include <jni.h>
#include <string>
#include <iostream>
#include <sstream>

#include <android/log.h>

#include <taglib/taglib_export.h>
#include <taglib/toolkit/tfilestream.h>
#include <taglib/toolkit/tfile.h>
#include <taglib/toolkit/tpropertymap.h>
#include <taglib/fileref.h>
#include <unistd.h>

extern "C"
JNIEXPORT jobject JNICALL
Java_com_github_adrijanrogan_taglib_android_AndroidAudioMetadataAccessor_readNative(
        JNIEnv *env,
        jobject,
        jint fd,
        jobject tags) {

    TagLib::FileStream stream(fd, true);
    if (!stream.isOpen()) {
        __android_log_write(ANDROID_LOG_ERROR, "TagLib", "FileStream is not open");
        close(fd);
        return nullptr;
    }

    TagLib::FileRef ref(&stream);
    if (ref.isNull()) {
        __android_log_write(ANDROID_LOG_ERROR, "TagLib", "FileRef is null");
        return nullptr;
    }

    // document
    const auto hashMapClass= env->FindClass("java/util/HashMap");
    if (hashMapClass == nullptr) {
        __android_log_write(ANDROID_LOG_ERROR, "TagLib", "hashMapClass is null");
        return nullptr;
    }

    // document
    const auto hashMapMethodPut = env->GetMethodID(hashMapClass, "put","(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
    if (hashMapMethodPut == nullptr) {
        __android_log_write(ANDROID_LOG_ERROR, "TagLib", "hashMapMethodPut is null");
        return nullptr;
    }

    // Copy all map key-value pairs
    for (const auto &item: ref.properties()) {
        // Skip empty values
        if (item.second.isEmpty()) {
            continue;
        }

        const auto key = env->NewStringUTF(item.first.toCString());
        const auto value = env->NewStringUTF(item.second[0].toCString());
        env->CallObjectMethod(tags, hashMapMethodPut, key, value);
        env->DeleteLocalRef(key);
        env->DeleteLocalRef(value);
    }

    const auto audioPropertiesClass = env->FindClass("com/github/adrijanrogan/taglib/AudioProperties");
    if (audioPropertiesClass == nullptr) {
        __android_log_write(ANDROID_LOG_ERROR, "TagLib", "audioPropertiesClass is null");
        return nullptr;
    }

    const auto audioPropertiesConstructor = env->GetMethodID(audioPropertiesClass, "<init>","(IIII)V");
    if (audioPropertiesConstructor == nullptr) {
        __android_log_write(ANDROID_LOG_ERROR, "TagLib", "audioPropertiesConstructor is null");
        return nullptr;
    }

    const auto properties = ref.audioProperties();
    if (properties == nullptr) {
        __android_log_write(ANDROID_LOG_ERROR, "TagLib", "properties is null");
        return nullptr;
    }

    return env->NewObject(
            audioPropertiesClass,
            audioPropertiesConstructor,
            properties->bitrate(),
            properties->channels(),
            properties->lengthInMilliseconds(),
            properties->sampleRate()
    );
}

extern "C"
JNIEXPORT void JNICALL
Java_com_github_adrijanrogan_taglib_android_AndroidAudioMetadataAccessor_writeNative(
        JNIEnv *env,
        jobject,
        jint fd,
        jobject tags) {

    TagLib::FileStream stream(fd, true);
    if (!stream.isOpen()) {
        __android_log_write(ANDROID_LOG_ERROR, "TagLib", "FileStream is not open");
        close(fd);
        return;
    }

    TagLib::FileRef ref(&stream);
    if (ref.isNull()) {
        __android_log_write(ANDROID_LOG_ERROR, "TagLib", "FileRef is null");
        return;
    }

    //////////////////////

    const auto hashMapClass= env->FindClass("java/util/HashMap");
    if (hashMapClass == nullptr) {
        __android_log_write(ANDROID_LOG_ERROR, "TagLib", "hashMapClass is null");
        return;
    }

    const auto hashMapEntrySetMethod = env->GetMethodID(hashMapClass, "entrySet", "()Ljava/util/Set;");
    if (hashMapEntrySetMethod == nullptr) {
        __android_log_write(ANDROID_LOG_ERROR, "TagLib", "hashMapEntrySetMethod is null");
        return;
    }

    const auto setClass = env->FindClass("java/util/Set");
    if (setClass == nullptr) {
        __android_log_write(ANDROID_LOG_ERROR, "TagLib", "setClass is null");
        return;
    }

    const auto setIteratorMethod = env->GetMethodID(setClass, "iterator", "()Ljava/util/Iterator;");

    const auto iteratorClass = env->FindClass("java/util/Iterator");
    const auto iteratorHasNextMethod = env->GetMethodID(iteratorClass, "hasNext", "()Z");
    const auto iteratorNextMethod = env->GetMethodID(iteratorClass, "next", "()Ljava/lang/Object;");

    const auto mapEntryClass = env->FindClass("java/util/Map$Entry");
    const auto mapEntryGetKeyMethod = env->GetMethodID(mapEntryClass, "getKey", "()Ljava/lang/Object;");
    const auto mapEntryGetValueMethod = env->GetMethodID(mapEntryClass, "getValue", "()Ljava/lang/Object;");

    const auto stringClass = env->FindClass("java/lang/String");
    const auto stringClassToStringMethod = env->GetMethodID(stringClass, "toString", "()Ljava/lang/String;");

    const auto objEntrySet = env->CallObjectMethod(tags, hashMapEntrySetMethod);
    const auto objIterator = env->CallObjectMethod(objEntrySet, setIteratorMethod);
    bool hasNext = (bool) env->CallBooleanMethod(objIterator, iteratorHasNextMethod);
    while (hasNext) {
        const auto entry = env->CallObjectMethod(objIterator, iteratorNextMethod);
        const auto key = (jstring) env->CallObjectMethod(entry, mapEntryGetKeyMethod);
        const auto value = (jstring) env->CallObjectMethod(entry, mapEntryGetValueMethod);

        std::string stringKey(env->GetStringUTFChars(key, nullptr));
        std::string stringValue(env->GetStringUTFChars(value, nullptr));

        std::stringstream ss;
        ss << stringKey << " -> " << stringValue;
        __android_log_write(ANDROID_LOG_DEBUG, "TagLib", ss.str().c_str());

        hasNext = (bool) env->CallBooleanMethod(objIterator, iteratorHasNextMethod);
    }
}
