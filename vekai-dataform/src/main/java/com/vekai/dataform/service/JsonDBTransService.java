//package com.vekai.dataform.service;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.ObjectReader;
//import com.vekai.base.dict.model.DictEntry;
//import com.vekai.base.dict.service.DictService;
//import com.vekai.dataform.autoconfigure.DataFormProperties;
//import com.vekai.dataform.model.DataForm;
//import com.vekai.dataform.service.impl.DataFormAdminServiceDBImpl;
//import com.vekai.runtime.kit.JSONKit;
//import org.apache.commons.io.FileUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.io.*;
//import java.nio.file.*;
//import java.nio.file.attribute.BasicFileAttributes;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.EnumSet;
//import java.util.List;
//
//@Service
//public class JsonDBTransService {
//
//    private final static Logger LOGGER = LoggerFactory.getLogger(JsonDBTransService.class);
//
//    @Autowired
//    DataFormProperties dataFormProperties;
//
//    @Autowired
//    DataFormAdminServiceDBImpl dataFormAdminService;
//
//    @Autowired
//    DictService dictService;
//
//    @Autowired
//    ObjectMapper objectMapper;
//
//    private volatile Dirs exportDirs;
//    private volatile Dirs importDirs;
//
//    private final static String DEFAULT_JSON_PATH = "src/main/resources/dbjson";
//
//    private Path getExportDir(String contextPath) {
//        String path = dataFormProperties.getExportDir();
//        if (null != path && !path.isEmpty()) return Paths.get(path);
//        return Paths.get(System.getProperty("user.dir"), contextPath, DEFAULT_JSON_PATH);
//    }
//
//    private Path getImportDir(String contextPath) {
//        return getExportDir(contextPath);
//    }
//
//    public void export2Json(String contextPath) throws IOException {
//        Path path = getExportDir(contextPath);
//        if (null == exportDirs)
//            exportDirs = prepareExportDir(path);
//        else
//            prepareExportDir(path);
//
//        List<DataForm> dataForms = dataFormAdminService.getCompleteDataForms();
//        if (null != dataForms && !dataForms.isEmpty()) {
//            String dfDir = exportDirs.dataformDir.toString();
//            dataForms.forEach(dataForm -> {
//                if (null == dataForm.getId() || dataForm.getId().isEmpty())
//                    return;
//                try {
//                    Path dfPath = Files.createFile(Paths.get(dfDir, dataForm.getId() + ".json"));
//                    writeJsonFile(dfPath, dataForm);
//                } catch (IOException ex) {
//                    LOGGER.warn(ex.getMessage());
//                }
//            });
//        }
//
//        List<DictEntry> dictEntries = dictService.getDictList();
//        if (null != dictEntries && !dictEntries.isEmpty()) {
//            String dicDir = exportDirs.dictionaryDir.toString();
//            dictEntries.forEach(dictEntry -> {
//                if (null == dictEntry.getCode() || dictEntry.getCode().isEmpty())
//                    return;
//                try {
//                    Path dicPath = Files.createFile(Paths.get(dicDir, dictEntry.getCode() + ".json"));
//                    writeJsonFile(dicPath, dictEntry);
//                } catch (IOException ex) {
//                    LOGGER.warn(ex.getMessage());
//                }
//            });
//        }
//    }
//
//    private void writeJsonFile(Path file, Object obj) throws IOException{
//        try(OutputStream outputStream = new BufferedOutputStream(
//                Files.newOutputStream(file, StandardOpenOption.CREATE))) {
//            objectMapper.writer().writeValue(outputStream, obj);
//            outputStream.close();
//        }
//    }
//
//    private Path ensureDirExist(Path path) throws IOException{
//        try {
//            return Files.createDirectories(path);
//        } catch (FileAlreadyExistsException ex) {
//            LOGGER.info(ex.getMessage());
//            return path;
//        }
//    }
//
//    private Dirs prepareExportDir(Path path) throws IOException{
//        if (Files.notExists(path)) {
//            path = ensureDirExist(path);
//            Path dicDir = ensureDirExist(Paths.get(path.toString(), dataFormProperties.getDictionaryDir()));
//            Path dataformDir = ensureDirExist(Paths.get(path.toString(), dataFormProperties.getDataformDir()));
//            return new Dirs(dicDir, dataformDir);
//        } else {
//            Path dicDir = Paths.get(path.toString(), dataFormProperties.getDictionaryDir());
//            Path dataformDir = Paths.get(path.toString(), dataFormProperties.getDataformDir());
//            if (Files.notExists(dicDir))
//                ensureDirExist(dicDir);
//            else
//                FileUtils.cleanDirectory(dicDir.toFile());
//            if (Files.notExists(dataformDir))
//                ensureDirExist(dataformDir);
//            else
//                FileUtils.cleanDirectory(dataformDir.toFile());
//
//            return new Dirs(dicDir, dataformDir);
//        }
//    }
//
//    @Transactional
//    public void import2DB(String contextPath) throws IOException{
//        if (null == importDirs) {
//            Path path = getImportDir(contextPath);
//            importDirs = new Dirs(Paths.get(path.toString(), dataFormProperties.getDictionaryDir()),
//                    Paths.get(path.toString(), dataFormProperties.getDataformDir()));
//        }
//        JsonFileVisitor<DataForm> dataFormJsonFileVisitor = new JsonFileVisitor<>(objectMapper.reader(),
//                DataForm.class, Collections.synchronizedList(
//                new ArrayList<>((int)Files.list(importDirs.dataformDir).count())));
//        Files.walkFileTree(importDirs.dataformDir, EnumSet.noneOf(FileVisitOption.class), 1, dataFormJsonFileVisitor);
//        if (null != dataFormJsonFileVisitor.objects && !dataFormJsonFileVisitor.objects.isEmpty()) {
//            dataFormAdminService.deleteAll();
//            dataFormJsonFileVisitor.objects.forEach(dataForm -> {
//                    try {
//                        dataFormAdminService.saveDataForm(dataForm, null);
//                    } catch (Exception ex) {
//                        System.err.println(JSONKit.toJsonString(dataForm));
//                        throw ex;
//                    }
//            });
//        }
//
//        JsonFileVisitor<DictEntry> dictEntryJsonFileVisitor = new JsonFileVisitor<>(objectMapper.reader(),
//                DictEntry.class, Collections.synchronizedList(
//                new ArrayList<>((int)Files.list(importDirs.dictionaryDir).count())));
//        Files.walkFileTree(importDirs.dictionaryDir, EnumSet.noneOf(FileVisitOption.class), 1, dictEntryJsonFileVisitor);
//        if (null != dictEntryJsonFileVisitor.objects && !dictEntryJsonFileVisitor.objects.isEmpty()) {
//            dictService.deleteAll();
//            dictEntryJsonFileVisitor.objects.forEach(dictEntry -> dictService.save(dictEntry));
//        }
//    }
//
//    private static class JsonFileVisitor<T> implements FileVisitor<Path> {
//
//        protected final ObjectReader objectReader;
//        protected final List<T> objects;
//
//        JsonFileVisitor(ObjectReader objectReader, Class<T> tClass, List<T> objects) {
//            this.objectReader = objectReader.forType(tClass);
//            this.objects = objects;
//        }
//
//        @Override
//        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
//            return FileVisitResult.CONTINUE;
//        }
//
//        @Override
//        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
//            String filePath = file.toString();
//            if (!filePath.endsWith(".json")) return FileVisitResult.CONTINUE;
//            try(InputStream inputStream = new BufferedInputStream(Files.newInputStream(file, StandardOpenOption.READ))) {
//                objects.add(objectReader.readValue(inputStream));
//                return FileVisitResult.CONTINUE;
//            }
//        }
//
//        @Override
//        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
//            LOGGER.warn(exc.getMessage());
//            return FileVisitResult.CONTINUE;
//        }
//
//        @Override
//        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
//            return FileVisitResult.CONTINUE;
//        }
//    }
//
//
//
//    private static class Dirs {
//        public final Path dictionaryDir;
//        public final Path dataformDir;
//
//        public Dirs(Path dictionaryDir, Path dataformDir) {
//            this.dictionaryDir = dictionaryDir;
//            this.dataformDir = dataformDir;
//        }
//    }
//
//}
