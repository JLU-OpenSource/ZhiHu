/*
 *    Copyright [2019] [chengjie.jlu@qq.com]
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.jlu.zhihu.util;

import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {

    public static void write2File(String content, String path) {
        try {
            FileWriter writer = new FileWriter(new File(path));
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            LoggerFactory.getLogger(FileUtil.class).error("write to file exception.", e);
        }
    }

    public static void deleteFile(String path) {
        File file = new File(path);
        if (!file.delete()) {
            throw new RuntimeException("delete file failed! path: " + path);
        }
    }
}
