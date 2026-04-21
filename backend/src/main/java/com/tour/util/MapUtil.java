package com.tour.util;

/**
 * 地图工具类 —— 距离计算占位符
 *
 * <p>使用 Haversine 公式计算两个 WGS-84 坐标点之间的球面距离。
 * 后续可在此扩展高德地图 API 的路线规划、逆地理编码等功能。</p>
 */
public class MapUtil {

    /** 地球平均半径（千米） */
    private static final double EARTH_RADIUS_KM = 6371.0;

    /**
     * 使用 Haversine 公式计算两个坐标点之间的直线距离
     *
     * @param lat1 起点纬度（度）
     * @param lon1 起点经度（度）
     * @param lat2 终点纬度（度）
     * @param lon2 终点经度（度）
     * @return 两点间距离（千米）
     */
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }
}
