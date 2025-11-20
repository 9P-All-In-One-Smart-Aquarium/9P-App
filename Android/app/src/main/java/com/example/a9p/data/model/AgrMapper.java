package com.example.a9p.data.model;

public class AgrMapper {

    public static AquariumStatus toAquariumStatus(AgrResponse response) {

        AquariumStatus status = new AquariumStatus();

        if (response == null || response.agr == null || response.agr.rsp == null)
            return status;

        for (AgrResponse.RspItem item : response.agr.rsp) {

            if (item.fr.contains("light")) {
                status.setLight(item.pc.cin.con);
            }
            else if (item.fr.contains("temp")) {
                status.setTemperature(item.pc.cin.con);
            }
            else if (item.fr.contains("wlevel")) {
                status.setWaterLevel(item.pc.cin.con);
            }
        }

        return status;
    }
    public static int[] mapFeedTime(FeedTimeResponse response) {
        try {
            if (response == null || response.cin == null)
                return null;

            String con = response.cin.con; // "[7,18]"

            con = con.replace("[", "").replace("]", "");
            String[] parts = con.split(",");

            if (parts.length == 2) {
                int t1 = Integer.parseInt(parts[0].trim());
                int t2 = Integer.parseInt(parts[1].trim());
                return new int[]{t1, t2};
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
