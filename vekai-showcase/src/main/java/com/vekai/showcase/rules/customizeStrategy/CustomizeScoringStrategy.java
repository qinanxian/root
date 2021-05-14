//package com.vekai.showcase.rules.customizeStrategy;
//
//import com.bstek.urule.Utils;
//import com.bstek.urule.model.scorecard.runtime.CellItem;
//import com.bstek.urule.model.scorecard.runtime.RowItem;
//import com.bstek.urule.model.scorecard.runtime.Scorecard;
//import com.bstek.urule.model.scorecard.runtime.ScoringStrategy;
//import com.bstek.urule.runtime.KnowledgeSession;
//import com.bstek.urule.runtime.rete.Context;
//import com.vekai.runtime.kit.BeanKit;
//import com.vekai.urule.entity.Customer;
//import org.springframework.stereotype.Component;
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
///**
// * Created by luyu on 2018/9/10.
// */
//@Component
//public class CustomizeScoringStrategy implements ScoringStrategy {
//
//    @Override
//    public Object calculate(Scorecard scorecard, Context context) {
//        BigDecimal result=new BigDecimal(0);
//        KnowledgeSession session = (KnowledgeSession) context.getWorkingMemory();
//        Map<String, Object> bizObjects = session.getAllFactsMap();
//        Customer customer = (Customer)bizObjects.get(context.getVariableCategoryClass("customer"));
//
//        CellItem variableCellItem = null;
//        CellItem valueCellItem = null;
//        CellItem scoreCellItem = null;
//
//        for(RowItem row:scorecard.getRowItems()){
//            if (row.getCellItems().size() > 0) {
//               int times = row.getCellItems().size() / 3;
//               for (int i = 0; i < times ; i++ ) {
//                   List<CellItem> tmpCellItem = row.getCellItems().stream().skip(3 * i)
//                           .limit(3).collect(Collectors.toList());
//                   for (CellItem cellItem : tmpCellItem) {
//                       if ("customizeVariable".equals(cellItem.getColName())) {
//                           variableCellItem = cellItem;
//                       } else if ("CustomizeValue".equals(cellItem.getColName())) {
//                           valueCellItem = cellItem;
//                       } else {
//                           scoreCellItem = cellItem;
//                       }
//                   }
//
//                   if (variableCellItem != null && !"0".equals(variableCellItem.getValue().toString())) {
//                       String bizValue = BeanKit.getPropertyValue(customer,variableCellItem.getValue().toString()).toString();
//                       if (bizValue.equals(valueCellItem.getValue().toString())) {
//                           result =  result.add(Utils.toBigDecimal(scoreCellItem.getValue()));
//                       }
//                   } else {
//                       result =  result.add(Utils.toBigDecimal(row.getScore()));
//                   }
//               }
//
//
//            } else {
//                result =  result.add(Utils.toBigDecimal(row.getScore()));
//            }
//        }
//        return result;
//    }
//}
