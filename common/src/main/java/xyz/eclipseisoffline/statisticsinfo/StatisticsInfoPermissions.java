package xyz.eclipseisoffline.statisticsinfo;

import xyz.eclipseisoffline.commonpermissionsapi.api.CommonPermissionNode;
import xyz.eclipseisoffline.commonpermissionsapi.api.CommonPermissions;

public interface StatisticsInfoPermissions {
    CommonPermissionNode STATISTICS_GET = createNode("get");
    CommonPermissionNode STATISTICS_GET_OTHER = createNode("get.other");
    CommonPermissionNode STATISTICS_LEADERBOARD = createNode("leaderboard");

    private static CommonPermissionNode createNode(String name) {
        return CommonPermissions.node(StatisticsInfo.getModdedIdentifier(name));
    }
}
