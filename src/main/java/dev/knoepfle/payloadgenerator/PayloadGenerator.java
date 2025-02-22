package dev.knoepfle.payloadgenerator;

import dev.knoepfle.ConfigurationManager;

import java.util.SplittableRandom;

class PayloadGenerator {

    private final ConfigurationManager configurationManager = ConfigurationManager.getInstance();

    final int errorRate = configurationManager.getInt("ERROR_RATE");
    final int duplicateRate = configurationManager.getInt("DUPLICATE_RATE");

    private final String[] firmwareVersions = {
        "M12QW_v07.03.164641-D52A1_v.0.171060",
        "M12QW_v07.03.164641-D52A1_v0.0.171530",
        "M14Q2_v12.09.163431-D54A1_v00.01.171530",
        "M12QW_v07.03.164641-D52A1_v0.1.172460",
        "M14Q2_v12.09.163431-D54A1_v00.02.172660",
        "M12QW_v07.03.164641-D52A1_v0.2.173570",
        "M14Q2_v12.09.163431-D54A1_v00.03.173570",
        "M12QW_v07.03.164641-D52A1_v0.3.174350",
        "M14Q2_v12.09.163431-D54A1_v00.04.174350",
        "M12QW_v07.03.164641-D52A1_v0.4.180620",
        "M12QW_v07.03.164641-D52A1_v0.4.180950",
        "M12QW_v07.03.164641-D52A1_v0.5.183970",
        "M12QW_v07.03.164641-D52A1_v1.0.190570",
        "M12QW_v07.03.164641-D52A1_v1.0.190960",
        "M12QW_v07.03.184831-D52A1_v1.0.190960",
        "M14Q2_v12.09.163431-D54A1_v00.05.180951",
        "M14Q2_v12.09.163431-D54A1_v00.06.183970",
        "M14Q2_v12.09.163431-D54A1_v01.00.190840",
        "M14Q2_v12.10.173541-D54A1_v01.00.190840",
        "M12QW_v16.02.183961-D52A1_v02.00.194040"
    };

    private final int firmwareVersionsLength = firmwareVersions.length;

    private final String[] locationTypes = {
        "2G",
        "3G",
        "4G",
        "GPS",
        "WIFI"
    };
    private final int locationTypesLength = locationTypes.length;

    StringBuilder payload;
    SplittableRandom randomGenerator;
    int random;
    int counter;
    int day1;
    int hour1;
    int minute1;
    int second1;
    int day2;
    int hour2;
    int day3;

    public PayloadGenerator(int offset) {
        this.payload = new StringBuilder();
        this.randomGenerator = new SplittableRandom(1L);
        this.counter = offset;
        this.random = randomGenerator.nextInt() & Integer.MAX_VALUE;
    }

    public String[] generate() {
        payload.setLength(0);
        int oldRandom = random;
        random = randomGenerator.nextInt() & Integer.MAX_VALUE;
        if (random % duplicateRate == 19) {
            random = oldRandom;
        } else {
            counter++;
        }

        day1 = (counter / 7200000) + 1; // Timestamp start
        hour1 = (counter % 7200000) / 300000; // Timestamp start
        minute1 = (counter % 300000) / 5000;
        second1 = (counter % 5000) / 84;

        day2 = ((counter + 300000) / 7200000) + 1;
        hour2 = ((counter + 300000) % 7200000) / 300000;

        day3 = day2 + 1;

        String id = "89011702272048" + (random % 300000 + 100000);


        payload.append("{\"cng_deviceId\":\"");
        payload.append(id);

        payload.append("\",\"timestamp\":\"");
        payload.append("2020-01-");
        if (day2 < 10) {
            payload.append("0");
        }
        payload.append(day2);
        payload.append("T");
        if (hour2 < 10) {
            payload.append("0");
        }
        payload.append(hour2);
        payload.append(":");
        if (minute1 < 10) {
            payload.append("0");
        }
        payload.append(minute1);
        payload.append(":");
        if (second1 < 10) {
            payload.append("0");
        }
        payload.append(second1);
        payload.append("+00:00");


        payload.append("\",\"timePeriodStart\":\"");
        payload.append("2020-01-");
        if (day1 < 10) {
            payload.append("0");
        }
        payload.append(day1);
        payload.append("T");
        if (hour1 < 10) {
            payload.append("0");
        }
        payload.append(hour1);
        payload.append(":");
        if (minute1 < 10) {
            payload.append("0");
        }
        payload.append(minute1);
        payload.append(":");
        if (second1 < 10) {
            payload.append("0");
        }
        payload.append(second1);
        payload.append("+00:00");

        payload.append("\",\"messageTimestamp\":\"");
        payload.append("2020-01-");
        if (day3 < 10) {
            payload.append("0");
        }
        payload.append(day3);
        payload.append("T00:00:03+00:00");


        payload.append("\",\"firmwareVersion\":");
        if (random % errorRate == 0) {
            payload.append("null");
        } else {
        payload.append('"');
        payload.append(firmwareVersions[random % firmwareVersionsLength]);
        payload.append('"');
        }

        payload.append(",\"temperature\":");
        if (random % errorRate == 1) {
            if(random % 4 == 0) {
                payload.append("null");
            } else if(random % 4 == 1) {
                payload.append("-40.0");
            } else if(random % 4 == 2) {
                payload.append("40.0");
            } else {
                payload.append("-");
                payload.append(random % 12);
                payload.append(".");
                payload.append(random % 10);
            }
        } else {
            payload.append(random % 12);
            payload.append(".");
            payload.append(random % 10);
        }

        payload.append(",\"powerState\":");
        payload.append(random % 2);

        payload.append(",\"coolerState\":");
        payload.append(random % 7);

        payload.append(",\"doorOpenCount\":");
        if (random % errorRate == 2) {
            payload.append("null");
        } else {
            payload.append(random % 100);
        }

        payload.append(",\"doorCloseCount\":");
        if (random % errorRate == 3) {
            payload.append("null");
        } else {
            payload.append(random % 100);
        }

        payload.append(",\"doorOpenTime\":");
        if (random % errorRate == 4) {
            if (random % 3 == 0) {
                payload.append("null");
            } else if (random % 3 == 1) {
                payload.append("-");
                payload.append(1537644156 + random % 1000000);
            } else {
                payload.append(1537644156 + random % 1000000);
            }
        } else {
            payload.append(random % 5000);
        }

        payload.append(",\"batteryLevel\":");
        if (random % errorRate == 5) {
            payload.append("null");
        } else {
            payload.append(random % 100);
        }

        payload.append(",\"latitude\":");
        if (random % errorRate == 6) {
            payload.append("null");
        } else {
            payload.append(random % 180);
            payload.append(".");
            payload.append(random % 1000000);
        }

        payload.append(",\"longitude\":");
        if (random % errorRate == 7) {
            payload.append("null");
        } else {
            payload.append(random % 179);
            payload.append(".");
            payload.append(random % 1000001);
        }

        payload.append(",\"locationType\":");
        if (random % errorRate == 8) {
            payload.append("null");
        } else {
            payload.append('"');
            payload.append(locationTypes[random % locationTypesLength]);
            payload.append('"');
        }

        payload.append(",\"locationConfidence\":");
        if (random % errorRate == 9) {
            payload.append("null");
        } else {
            payload.append(0.5 + (random % 50) * 0.01);
        }

        payload.append(",\"wifiCount\":");
        if (random % errorRate == 10) {
            payload.append("null");
        } else {
            payload.append(random % 20);
        }

        payload.append(",\"mobileCellId\":");
        if (random % errorRate == 11) {
            payload.append("null");
        } else {
            payload.append('"');
            payload.append(1000000 + random % 1000000);
            payload.append('"');
        }

        payload.append(",\"mobileCellType\":");
        if (random % errorRate == 12) {
            payload.append("null");
        } else {
            payload.append('"');
            payload.append(random % 3 + 2);
            payload.append("G");
            payload.append('"');
        }

        payload.append(",\"mobileMNC\":");
        if (random % errorRate == 13) {
            payload.append("null");
        } else {
            payload.append("1");
        }

        payload.append(",\"mobileMCC\":");
        if (random % errorRate == 14) {
            payload.append("null");
        } else {
            payload.append(random % 300);
        }

        payload.append(",\"mobileRSSI\":");
        if (random % errorRate == 15) {
            payload.append("null");
        } else {
            payload.append("-");
            payload.append(random % 99);
        }

        payload.append(",\"mobileLac\":");
        if (random % errorRate == 16) {
            payload.append("null");
        } else {
            payload.append("1");
        }

        payload.append(",\"messageType\":");
        payload.append("0");

        payload.append(",\"plausibilityState\":");
        payload.append("0");

        payload.append(",\"traceId\":\"");
        payload.append(random);

        payload.append("\"}");

        return new String[]{id, payload.toString()};
    }

}
