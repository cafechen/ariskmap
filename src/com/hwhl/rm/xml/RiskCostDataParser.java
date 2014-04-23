package com.hwhl.rm.xml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.hwhl.rm.model.RiskCost;
import com.hwhl.rm.util.StrUtil;

/**
 * 对目录文件进行解析
 */
public class RiskCostDataParser {

	public List<RiskCost> getItems(InputStream inStream) throws Throwable {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		ItemParser itemParser = new ItemParser();
		parser.parse(inStream, itemParser);
		inStream.close();
		return itemParser.getItems();
	}

	private final class ItemParser extends DefaultHandler {
		private List<RiskCost> items = null;
		private String tag = null;
		private RiskCost item = null;
		private String lastTag = "";

		public List<RiskCost> getItems() {
			return items;
		}

		@Override
		public void startDocument() throws SAXException {
			items = new ArrayList<RiskCost>();
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			if ("Risk".equals(localName)) {
				item = new RiskCost();
				// project.setId(new Integer(attributes.getValue(0)));
			}
			tag = localName;
		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			if (tag != null) {
				String data = new String(ch, start, length);
				if ("ID".equals(tag)) {
					if (lastTag.equals(tag))
						item.setId(StrUtil.nullToStr(item.getId()) + data);
					else
						item.setId(data);
					lastTag = tag;
				} else if ("riskName".equals(tag)) {
					if (lastTag.equals(tag))
						item.setRiskName(StrUtil.nullToStr(item.getRiskName())
								+ data);
					else
						item.setRiskName(data);
					lastTag = tag;
				} else if ("riskCode".equals(tag)) {
					if (lastTag.equals(tag))
						item.setRiskCode(StrUtil.nullToStr(item.getRiskCode())
								+ data);
					else
						item.setRiskCode(data);
					lastTag = tag;
				} else if ("riskType".equals(tag)) {
					if (lastTag.equals(tag))
						item.setRiskType(StrUtil.nullToStr(item.getRiskType())
								+ data);
					else
						item.setRiskType(data);
					lastTag = tag;
				} else if ("beforeGailv".equals(tag)) {
					if (lastTag.equals(tag))
						item.setBeforeGailv(StrUtil.nullToStr(item
								.getBeforeGailv()) + data);
					else
						item.setBeforeGailv(data);
					lastTag = tag;
				} else if ("beforeAffect".equals(tag)) {
					if (lastTag.equals(tag))
						item.setBeforeAffect(StrUtil.nullToStr(item
								.getBeforeAffect()) + data);
					else
						item.setBeforeAffect(data);
					lastTag = tag;
				} else if ("beforeAffectQi".equals(tag)) {
					if (lastTag.equals(tag))
						item.setBeforeAffectQi(StrUtil.nullToStr(item
								.getBeforeAffectQi()) + data);
					else
						item.setBeforeAffectQi(data);
					lastTag = tag;
				} else if ("manaChengben".equals(tag)) {
					if (lastTag.equals(tag))
						item.setManaChengben(StrUtil.nullToStr(item
								.getManaChengben()) + data);
					else
						item.setManaChengben(data);
					lastTag = tag;
				} else if ("afterGailv".equals(tag)) {
					if (lastTag.equals(tag))
						item.setAfterGailv(StrUtil.nullToStr(item
								.getAfterGailv()) + data);
					else
						item.setAfterGailv(data);
					lastTag = tag;
				} else if ("afterAffect".equals(tag)) {
					if (lastTag.equals(tag))
						item.setAfterAffect(StrUtil.nullToStr(item
								.getAfterAffect()) + data);
					else
						item.setAfterAffect(data);
					lastTag = tag;
				} else if ("afterQi".equals(tag)) {
					if (lastTag.equals(tag))
						item.setAfterQi(StrUtil.nullToStr(item.getAfterQi())
								+ data);
					else
						item.setAfterQi(data);
					lastTag = tag;
				} else if ("affectQi".equals(tag)) {
					if (lastTag.equals(tag))
						item.setAffectQi(StrUtil.nullToStr(item.getAffectQi())
								+ data);
					else
						item.setAffectQi(data);
					lastTag = tag;
				} else if ("shouyi".equals(tag)) {
					if (lastTag.equals(tag))
						item.setShouyi(StrUtil.nullToStr(item.getShouyi())
								+ data);
					else
						item.setShouyi(data);
					lastTag = tag;
				} else if ("jingshouyi".equals(tag)) {
					if (lastTag.equals(tag))
						item.setJingshouyi(StrUtil.nullToStr(item
								.getJingshouyi()) + data);
					else
						item.setJingshouyi(data);
					lastTag = tag;
				} else if ("bilv".equals(tag)) {
					if (lastTag.equals(tag))
						item.setBilv(StrUtil.nullToStr(item.getBilv()) + data);
					else
						item.setBilv(data);
					lastTag = tag;
				} else if ("projectId".equals(tag)) {
					if (lastTag.equals(tag))
						item.setProjectId(StrUtil.nullToStr(item.getProjectId())
								+ data);
					else
						item.setProjectId(data);
					lastTag = tag;
				} else if ("pageid".equals(tag)) {
					if (lastTag.equals(tag))
						item.setPageid(StrUtil.nullToStr(item.getPageid())
								+ data);
					else

						item.setPageid(data);
					lastTag = tag;
				} else if ("riskvecorid".equals(tag)) {
					if (lastTag.equals(tag))
						item.setRiskvecorid(StrUtil.nullToStr(item
								.getRiskvecorid()) + data);
					else
						item.setRiskvecorid(data);
					lastTag = tag;
				} else if ("chanceVecorid".equals(tag)) {
					if (lastTag.equals(tag))
						item.setChanceVecorid(StrUtil.nullToStr(item
								.getChanceVecorid()) + data);
					else
						item.setChanceVecorid(data);
					lastTag = tag;
				}
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			if ("Risk".equals(localName)) {
				items.add(item);
				item = null;
			}
			tag = null;
		}
	}
}
