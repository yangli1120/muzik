package crazysheep.io.materialmusic.bean;

import java.util.List;

/**
 * album dto
 *
 * Created by crazysheep on 15/12/21.
 */
public class AlbumDto extends BaseDto {

    /*
     *
        {
          "rating": {
            "max": 10,
            "average": "9.0",
            "numRaters": 322,
            "min": 0
          },
          "author": [
            {
              "name": "Ataraxia"
            }
          ],
          "alt_title": "",
          "image": "https://img1.doubanio.com/spic/s1506689.jpg",
          "title": "A Calliope... Collection",
          "mobile_link": "http://m.douban.com/music/subject/1499824/",
          "summary": "Ataraxia / 专辑 / 2001-12 / Future Insights / CD",
          "attrs": {
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
          },
          "alt": "http://music.douban.com/subject/1499824/",
          "id": "1499824",
          "tags": [
            {
              "count": 93,
              "name": "darkwave"
            },
            {
              "count": 68,
              "name": "Neo-Classical"
            },
            {
              "count": 63,
              "name": "ATARAXIA"
            },
            {
              "count": 42,
              "name": "意大利"
            },
            {
              "count": 23,
              "name": "Neo-Folk"
            },
            {
              "count": 19,
              "name": "中世纪民谣"
            },
            {
              "count": 17,
              "name": "Medieval"
            },
            {
              "count": 13,
              "name": "Ataraxia"
            }
          ]
        }
    * */

    public RatingDto rating;
    public List<ArtistDto> author;
    public String image;
    public String title;
    public String mobile_link;
    public String summary;
    public AlbumAttrsDto attrs;
    public String alt;
    public long id;
    public List<TagDto> tags;

}
