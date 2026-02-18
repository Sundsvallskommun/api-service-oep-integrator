package se.sundsvall.oepintegrator.util;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static org.jsoup.Jsoup.parse;
import static org.jsoup.parser.Parser.xmlParser;
import static us.codecraft.xsoup.Xsoup.compile;

public final class XPathUtil {

	private XPathUtil() {}

	public static Document parseXmlDocument(final byte[] xml) {
		return parse(new String(xml, ISO_8859_1), xmlParser());
	}

	public static Elements evaluateXPath(final Document doc, final String expression) {
		return compile(expression).evaluate(doc).getElements();
	}

	public static Elements evaluateXPath(final Element element, final String expression) {
		return compile(expression).evaluate(element).getElements();
	}
}
