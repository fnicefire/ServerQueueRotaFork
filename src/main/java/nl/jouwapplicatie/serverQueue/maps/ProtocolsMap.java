package nl.jouwapplicatie.serverQueue.maps;

public class ProtocolsMap {

    public static String mapProtocolToVersion(int protocol) {
        if (protocol < 47) return "UNSUPPORTED";

        // 1.8.x
        if (protocol == 47) return "1.8.x";

        // 1.9.x
        if (protocol <= 110) {
            switch (protocol) {
                case 107: return "1.9";
                case 108: return "1.9.1";
                case 109: return "1.9.2";
                case 110: return "1.9.3-1.9.4";
            }
        }

        // 1.10.x
        if (protocol == 210) return "1.10.x";

        // 1.11.x
        if (protocol == 315) return "1.11";
        if (protocol == 316) return "1.11.1-1.11.2";

        // 1.12.x
        if (protocol == 335) return "1.12";
        if (protocol == 338) return "1.12.1";
        if (protocol == 340) return "1.12.2";

        // 1.13.x
        if (protocol == 393) return "1.13";
        if (protocol == 401) return "1.13.1";
        if (protocol == 404) return "1.13.2";

        // 1.14.x
        if (protocol == 477) return "1.14";
        if (protocol == 480) return "1.14.1";
        if (protocol == 485) return "1.14.2";
        if (protocol == 490) return "1.14.3";
        if (protocol == 498) return "1.14.4";

        // 1.15.x
        if (protocol == 573) return "1.15";
        if (protocol == 575) return "1.15.1";
        if (protocol == 578) return "1.15.2";

        // 1.16.x
        if (protocol == 735) return "1.16";
        if (protocol == 736) return "1.16.1";
        if (protocol == 751) return "1.16.2";
        if (protocol == 753) return "1.16.3";
        if (protocol == 754) return "1.16.4-1.16.5";

        // 1.17.x
        if (protocol == 755) return "1.17-1.17.1";

        // 1.18.x
        if (protocol == 757) return "1.18-1.18.1";
        if (protocol == 758) return "1.18.2";

        // 1.19.x
        if (protocol == 759) return "1.19";
        if (protocol == 760) return "1.19.2";
        if (protocol == 761) return "1.19.3";
        if (protocol == 762) return "1.19.4";

        // 1.20.x
        if (protocol == 763) return "1.20-1.20.1";
        if (protocol == 764) return "1.20.2";
        if (protocol == 765) return "1.20.3-1.20.4";
        if (protocol == 766) return "1.20.5-1.20.6";

        // 1.21.x
        if (protocol == 767) return "1.21-1.21.1";
        if (protocol == 768) return "1.21.2-1.21.3";
        if (protocol == 769) return "1.21.4";
        if (protocol == 770) return "1.21.5";
        if (protocol == 771) return "1.21.6";
        if (protocol == 772) return "1.21.7-1.21.8";
        if (protocol == 773) return "1.21.9-1.21.10";

        return "UNSUPPORTED";
    }
}
