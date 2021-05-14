package com.vekai.auth.admin.common;

import com.vekai.auth.entity.Org;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * on 18-1-6.
 */
public class OrgTreeTest {

    private static List<Org> originOrgList = new ArrayList<>();
//
//    @Test
//    public void testBuildOrgAsTree() {
//        List<OrgTreeNode> orgTrees = new ArrayList<>();
//        List<Org> orgList = distinctAndSortByKey(originOrgList, Org::getSortCode);
//        List<String> usedSortCodes = new ArrayList<>();
//        OrgTreeNode orgTree;
//        for (int i = 0; i < orgList.size(); i++) {
//            Org curOrg = orgList.get(i);
//            String curSortCode = curOrg.getSortCode();
//
//            if (usedSortCodes.contains(curSortCode)) {
//                continue;
//            }
//            usedSortCodes.add(curSortCode);
//
//            orgTree = new TreeNodeWrapper<>();
//            orgTree.setValue(curOrg);
//
//            List<TreeNodeWrapper<Org>> childrenOrgTrees =
//                getChildrenOrgTree(orgList, usedSortCodes, curSortCode);
//            if (!childrenOrgTrees.isEmpty()) {
//                orgTree.setChildren(childrenOrgTrees);
//            }
//
//            orgTrees.add(orgTree);
//        }
//
//        Assert.assertNotNull(orgTrees);
//    }
//
//    private List<TreeNodeWrapper<Org>> getChildrenOrgTree(List<Org> orgList, List<String> usedSortCodes,
//                                                          String sortCode) {
//        List<TreeNodeWrapper<Org>> result = new ArrayList<>();
//        TreeNodeWrapper<Org> childrenOrgTree;
//        for (int i = 0; i < orgList.size(); i++) {
//            Org curOrg = orgList.get(i);
//            String curSortCode = curOrg.getSortCode();
//
//            if (curOrg.getSortCode().equals(sortCode) || !curSortCode.startsWith(sortCode)) {
//                continue;
//            }
//
//            if (usedSortCodes.contains(curSortCode)) {
//                continue;
//            }
//            usedSortCodes.add(curSortCode);
//
//            childrenOrgTree = new TreeNodeWrapper<>();
//            childrenOrgTree.setValue(curOrg);
//
//            List<TreeNodeWrapper<Org>> childrenOrgTrees =
//                getChildrenOrgTree(orgList, usedSortCodes, curSortCode);
//            if (!childrenOrgTrees.isEmpty()) {
//                childrenOrgTree.setChildren(childrenOrgTrees);
//            }
//
//            result.add(childrenOrgTree);
//
//        }
//
//        return result;
//    }



    private <T> List<T> distinctAndSortByKey(List<T> source,
                                             Function<? super T, String> keyExtractor) {
        List<T> target = source.stream().filter(createDistinctPredicateByKey(keyExtractor))
                .collect(Collectors.toList());
        target.sort(Comparator.comparing(keyExtractor));
        return target;
    }


    private <T> Predicate<T> createDistinctPredicateByKey(
            Function<? super T, String> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    static {
        Org org = new Org();
        org.setId("1");
        org.setName("1");
        org.setSortCode("1");
        originOrgList.add(org);

        org = new Org();
        org.setId("1010");
        org.setName("1010");
        org.setSortCode("1010");
        originOrgList.add(org);

        org = new Org();
        org.setId("1040010");
        org.setName("1040010");
        org.setSortCode("1040010");
        originOrgList.add(org);

        org = new Org();
        org.setId("1020");
        org.setName("1020");
        org.setSortCode("1020");
        originOrgList.add(org);

        org = new Org();
        org.setId("1030");
        org.setName("1030");
        org.setSortCode("1030");
        originOrgList.add(org);

        org = new Org();
        org.setId("2");
        org.setName("2");
        org.setSortCode("2");
        originOrgList.add(org);

        org = new Org();
        org.setId("1010010");
        org.setName("1010010");
        org.setSortCode("1010010");
        originOrgList.add(org);

        org = new Org();
        org.setId("1010020");
        org.setName("1010020");
        org.setSortCode("1010020");
        originOrgList.add(org);
    }
}
