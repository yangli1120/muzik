package crazysheep.io.materialmusic.bean;

import java.util.List;

/**
 * album attrs dto
 *
 * Created by crazysheep on 15/12/21.
 */
public class AlbumAttrsDto extends BaseDto {

    /*
    * {
            "publisher": [
              "Future Insights"
            ],
            "singer": [
              "Ataraxia"
            ],
            "discs": [
              "1"
            ],
            "radio_url": "http://douban.fm/?context=channel:0|subject_id:1499824",
            "pubdate": [
              "2001-12"
            ],
            "title": [
              "A Calliope... Collection"
            ],
            "media": [
              "CD"
            ],
            "tracks": [
              "01 Prophetia\n02 Elevazione\n03 Lubna\n04 Ondine\n05 Verdigris Wounds\n06 Rocking Chair of Dreams\n07 Clytaemestra\n08 Le Ore Rosa Di Mazenderan\n09 Belle Rose Porporine\n10 Scarlet Leaves\n11 Orlando (...a Male)\n12 Oduarpa (Live)\n13 Aperlae\n14 I Love Every Waving Thing\n15 Arcana Eco\n16 A Calliope"
            ],
            "version": [
              "专辑"
            ],
            "songs": [
              {
                "index": 1,
                "name": "track",
                "title": "Prophetia"
              },
              {
                "index": 2,
                "name": "track",
                "title": "Elevazione"
              },
              {
                "index": 3,
                "name": "track",
                "title": "Lubna"
              },
              {
                "index": 4,
                "name": "track",
                "title": "Ondine"
              },
              {
                "index": 5,
                "name": "track",
                "title": "Verdigris Wounds"
              },
              {
                "index": 6,
                "name": "track",
                "title": "Rocking Chair Of Dreams"
              },
              {
                "index": 7,
                "name": "track",
                "title": "Clytaemestra"
              },
              {
                "index": 8,
                "name": "track",
                "title": "Le Ore Rosa Di Mazenderan"
              },
              {
                "index": 9,
                "name": "track",
                "title": "Belle Rose Porporine"
              },
              {
                "index": 10,
                "name": "track",
                "title": "Scarlet Leaves"
              },
              {
                "index": 11,
                "name": "track",
                "title": "Orlando (... A Male)"
              },
              {
                "index": 12,
                "name": "track",
                "title": "Oduarpa (Live)"
              },
              {
                "index": 13,
                "name": "track",
                "title": "Aperlae"
              },
              {
                "index": 14,
                "name": "track",
                "title": "I Love Every Waving Thing"
              },
              {
                "index": 15,
                "name": "track",
                "title": "Arcana Eco"
              },
              {
                "index": 16,
                "name": "track",
                "title": "A Calliope"
              }
            ]
          }
    * */
    public List<String> publisher;
    public List<String> singer;
    public List<String> discs;
    public String radio_url;
    public List<String> pubdate;
    public List<String> title;
    public List<String> media;
    public List<String> tracks;
    public List<String> version;


}
