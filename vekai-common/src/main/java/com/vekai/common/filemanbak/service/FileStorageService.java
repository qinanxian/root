package com.vekai.common.filemanbak.service;

import com.vekai.common.filemanbak.bean.FileStoreResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

//
//  when store:
//      for FileSystemStorageService just return the input parameter fileName
//      for FastDFS, return the grouoname/remotefilename(dfs file_id)
//      if not MultipartFile, the history won't be taken into consideration
// when open
//      fileId parameter is just the return value when store
//

public interface FileStorageService {

    // file history shall be taken into consideration
    // historyVersion is the current version number.
    FileStoreResult storeAlsoHistory(MultipartFile multipartFile, String fileEntityId, String lastFileName,
                                     String lastVersion, String lastSignature);

    String store(InputStream input, String fileName, String fileEntityId, String lastFileName);

    String copy(String srcFileEntityId, String srcFileName, long fileSize, String destFileEntityId, String destFileName);

    String store(byte[] inputBytes, String fileName, String fileEntityId, String lastFileName);

    // just return InputStream may or may not BufferInputStream
    InputStream open(String fileEntityId, String fileName, long knownSize);

    InputStream openHist(String fileEntityId, String fileName, String versionCode, long knownSize);

    void openAction(String fileEntityId, String fileName, long knownSize, DownloadCB downloadCallback);

    void openHistAction(String fileEntityId, String fileName, String versionCode, long knownSize, DownloadCB downloadCallback);

    void delete(String fileEntityId, String fileName);

    void deleteHist(String fileEntityId, String fileName, String versionCode);

    default RuntimeException wrapIfNecessary(Exception ex) {
        if (ex instanceof RuntimeException)
            return (RuntimeException)ex;
        else
            return new RuntimeException(ex);
    }
}
