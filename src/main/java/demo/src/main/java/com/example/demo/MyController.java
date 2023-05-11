/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.example.demo;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;

@Controller
public class MyController {
    //codice per gestire la compressione tramite la libreria
    @PostMapping(value = "/compress")
    public  ResponseEntity<Resource> submit(@RequestParam("fileToCompress") MultipartFile file) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            try (TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(baos)) {
                TarArchiveEntry entry = new TarArchiveEntry(file.getOriginalFilename());
                entry.setSize(file.getSize());
                tarOutput.putArchiveEntry(entry);
                InputStream inputStream = file.getInputStream();
                StreamUtils.copy(inputStream, tarOutput);
                inputStream.close();
                tarOutput.closeArchiveEntry();
            }
            byte[] compressedBytes = baos.toByteArray();
            ByteArrayResource resource = new ByteArrayResource(compressedBytes);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=file.tar");
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
            headers.add(HttpHeaders.CONTENT_LENGTH, Integer.toString(compressedBytes.length));
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);

        }
    }
}
