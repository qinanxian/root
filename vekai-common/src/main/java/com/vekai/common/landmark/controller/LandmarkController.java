package com.vekai.common.landmark.controller;

import com.vekai.common.landmark.entity.CmonLandmarkItem;
import com.vekai.common.landmark.model.LandmarkItemNode;
import com.vekai.common.landmark.service.LandmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/landMark")
public class LandmarkController {
    @Autowired
    LandmarkService landmarkService;

    @GetMapping("/getLandmarkItems/{landMarkId}")
    public List<LandmarkItemNode> getLandmarkItems(@PathVariable("landMarkId") String landMarkId){
        List<CmonLandmarkItem> itemList = landmarkService.getLandmarkItems(landMarkId);
        return landmarkService.parseLandmarkTree(itemList);
    }

    @GetMapping("/getLandmarkItems/{objectId}/{objectType}")
    public List<LandmarkItemNode> getLandmarkItems(@PathVariable("objectId") String objectId,@PathVariable("objectType") String objectType){
        List<CmonLandmarkItem> itemList = landmarkService.getLandmarkItems(objectId, objectType);
        return landmarkService.parseLandmarkTree(itemList);
    }
}
