package com.hwhl.rm.xml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.hwhl.rm.model.Map;
import com.hwhl.rm.util.StrUtil;

/**
 * 地图数据解析
 */
public class MapDataParser {
	public List<Map> getMaps(InputStream inStream) throws Throwable {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		MapParser mapParser = new MapParser();
		parser.parse(inStream, mapParser);
		inStream.close();
		return mapParser.getMaps();
	}

	private final class MapParser extends DefaultHandler {
		private List<Map> maps = null;
		private String tag = null;
		private Map map = null;
		private String lastTag = "";

		public List<Map> getMaps() {
			return maps;
		}

		@Override
		public void startDocument() throws SAXException {
			maps = new ArrayList<Map>();
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			if ("Risk".equals(localName)) {
				map = new Map();
				// map.setId(new Integer(attributes.getValue(0)));
			}
			tag = localName;
		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			if (tag != null) {
				String data = new String(ch, start, length);
				if ("id".equals(tag)) {
					if (lastTag.equals(tag))
						map.setId(StrUtil.nullToStr(map.getId()) + data);
					else
						map.setId(data);
					lastTag = tag;
				} else if ("projectId".equals(tag)) {
					if (lastTag.equals(tag))
						map.setProjectId(StrUtil.nullToStr(map.getProjectId())
								+ data);
					else
						map.setProjectId(data);
					lastTag = tag;
				} else if ("objectId".equals(tag)) {
					if (lastTag.equals(tag))
						map.setObjectId(StrUtil.nullToStr(map.getObjectId())
								+ data);
					else
						map.setObjectId(data);
					lastTag = tag;
				} else if ("title".equals(tag)) {
					map.setTitle(data);
					lastTag = tag;
				} else if ("positionX".equals(tag)) {
					if (lastTag.equals(tag))
						map.setPositionX(StrUtil.nullToStr(map.getPositionX())
								+ data);
					else
						map.setPositionX(data);
					lastTag = tag;
				} else if ("positionY".equals(tag)) {
					if (lastTag.equals(tag))
						map.setPositionY(StrUtil.nullToStr(map.getPositionY())
								+ data);
					else
						map.setPositionY(data);
					lastTag = tag;
				} else if ("width".equals(tag)) {
					if (lastTag.equals(tag))
						map.setWidth(StrUtil.nullToStr(map.getWidth()) + data);
					else
						map.setWidth(data);
					lastTag = tag;
				} else if ("height".equals(tag)) {
					if (lastTag.equals(tag))
						map.setHeight(StrUtil.nullToStr(map.getHeight()) + data);
					else
						map.setHeight(data);
					lastTag = tag;
				} else if ("isline".equals(tag)) {
					map.setIsline(data);
					lastTag = tag;
				} else if ("fromWho".equals(tag)) {
					if (lastTag.equals(tag))
						map.setFromWho(StrUtil.nullToStr(map.getFromWho())
								+ data);
					else
						map.setFromWho(data);
					lastTag = tag;
				} else if ("toWho".equals(tag)) {
					if (lastTag.equals(tag))
						map.setToWho(StrUtil.nullToStr(map.getToWho()) + data);
					else
						map.setToWho(data);
					lastTag = tag;
				} else if ("picPng".equals(tag)) {
					if (lastTag.equals(tag))
						map.setPicPng(StrUtil.nullToStr(map.getPicPng()) + data);
					else
						map.setPicPng(data);
					lastTag = tag;
				} else if ("picEmz".equals(tag)) {
					if (lastTag.equals(tag))
						map.setPicEmz(StrUtil.nullToStr(map.getPicEmz()) + data);
					else
						map.setPicEmz(data);
					lastTag = tag;
				} else if ("belongPage".equals(tag)) {
					if (lastTag.equals(tag))
						map.setBelongPage(StrUtil.nullToStr(map.getBelongPage())
								+ data);
					else
						map.setBelongPage(data);
					lastTag = tag;
				} else if ("Obj_maptype".equals(tag)) {
					if (lastTag.equals(tag))
						map.setObj_maptype(StrUtil.nullToStr(map
								.getObj_maptype()) + data);
					else
						map.setObj_maptype(data);
					lastTag = tag;
				} else if ("Obj_belongwho".equals(tag)) {
					if (lastTag.equals(tag))
						map.setObj_belongwho(StrUtil.nullToStr(map
								.getObj_belongwho()) + data);
					else
						map.setObj_belongwho(data);
					lastTag = tag;
				} else if ("Obj_remark".equals(tag)) {
					if (lastTag.equals(tag))
						map.setObj_remark(StrUtil.nullToStr(map.getObj_remark())
								+ data);
					else
						map.setObj_remark(data);
					lastTag = tag;
				} else if ("Obj_db_id".equals(tag)) {
					if (lastTag.equals(tag))
						map.setObj_db_id(StrUtil.nullToStr(map.getObj_db_id())
								+ data);
					else
						map.setObj_db_id(data);
					lastTag = tag;
				} else if ("Obj_riskTypeStr".equals(tag)) {
					if (lastTag.equals(tag))
						map.setObj_riskTypeStr(StrUtil.nullToStr(map
								.getObj_riskTypeStr()) + data);
					else
						map.setObj_riskTypeStr(data);
					lastTag = tag;
				} else if ("Obj_other1".equals(tag)) {
					if (lastTag.equals(tag))
						map.setObj_other1(StrUtil.nullToStr(map.getObj_other1())
								+ data);
					else
						map.setObj_other1(data);
					lastTag = tag;
				} else if ("Obj_other2".equals(tag)) {
					if (lastTag.equals(tag))
						map.setObj_other2(StrUtil.nullToStr(map.getObj_other2())
								+ data);
					else
						map.setObj_other2(data);
					lastTag = tag;
				} else if ("Obj_data1".equals(tag)) {
					if (lastTag.equals(tag))
						map.setObj_data1(StrUtil.nullToStr(map.getObj_data1())
								+ data);
					else
						map.setObj_data1(data);
					lastTag = tag;
				} else if ("Obj_data2".equals(tag)) {
					if (lastTag.equals(tag))
						map.setObj_data2(StrUtil.nullToStr(map.getObj_data2())
								+ data);
					else
						map.setObj_data2(data);
					lastTag = tag;
				} else if ("Obj_data3".equals(tag)) {
					if (lastTag.equals(tag)) {
						map.setObj_data3(StrUtil.nullToStr(map.getObj_data3())
								+ data);
					} else
						map.setObj_data3(data);
					lastTag = tag;
				} else if ("lineType".equals(tag)) {
					if (lastTag.equals(tag))
						map.setLineType(StrUtil.nullToStr(map.getLineType())
								+ data);
					else
						map.setLineType(data);
					lastTag = tag;
				} else if ("lineType2".equals(tag)) {
					if (lastTag.equals(tag))
						map.setLineType2(StrUtil.nullToStr(map.getLineType2())
								+ data);
					else
						map.setLineType2(data);
				} else if ("isDel".equals(tag)) {
					if (lastTag.equals(tag))
						map.setIsDel(StrUtil.nullToStr(map.getIsDel()) + data);
					else
						map.setIsDel(data);
					lastTag = tag;
				} else if ("linkPics".equals(tag)) {
					if (lastTag.equals(tag))
						map.setLinkPics(StrUtil.nullToStr(map.getLinkPics())
								+ data);
					else
						map.setLinkPics(data);
					lastTag = tag;
				} else if ("cardPic".equals(tag)) {
					if (lastTag.equals(tag))
						map.setCardPic(StrUtil.nullToStr(map.getCardPic())
								+ data);
					else
						map.setCardPic(data);
					lastTag = tag;
				} else if ("belonglayers".equals(tag)) {
                    if (lastTag.equals(tag))
                        map.setBelongLayers(StrUtil.nullToStr(map.getBelongLayers())
                                + data);
                    else
                        map.setBelongLayers(data);
                    lastTag = tag;
                }

			}
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			if ("Risk".equals(localName)) {
				maps.add(map);
				map = null;
			}
			tag = null;
		}
	}
}
