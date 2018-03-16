package org.apache.xalan.xsltc.compiler.util;

import java.util.Vector;
import java.util.Enumeration;
import java.util.ResourceBundle;

public final class ErrorMessages_no extends ErrorMessages {
    
    private static final String errorMessages[] = { 
	"En fil kan bare innehold ett stilark.",
	"<xsl:template> ''{0}'' er allerede definert i dette stilarket.",
	"<xsl:template> ''{0}'' er ikke definert i dette stilarket.",
	"Variabel ''{0}'' er allerede definert.",
	"Variabel eller parameter ''{0}'' er ikke definert.",
	"Finner ikke klassen ''{0}''.",
	"Finner ikke ekstern funksjon ''{0}'' (m\u00e5 v\00e6re deklarert b\u00e5de 'static' og 'public').",
	"Kan ikke konvertere argument/retur type i kall til funksjon ''{0}''",
	"Finner ikke fil eller URI ''{0}''.",
	"Ugyldig URI ''{0}''.",
	"Kan ikke \u00e5pne fil eller URI ''{0}''.",
	"Forvented <xsl:stylesheet> eller <xsl:transform> element.",
	"Prefiks ''{0}'' er ikke deklarert.",
	"Kunne ikke resolvere kall til funksjon ''{0}''.",
	"Argument til ''{0}'' m\u00e5 v\00e6re ordrett tekst.",
	"Kunne ikke tolke XPath uttrykk ''{0}''.",
	"N\u00f8dvendig attributt ''{0}'' er ikke deklarert.",
	"Ugyldig bokstav/tegn ''{0}'' i XPath uttrykk.",
	"Ugyldig navn ''{0}'' for prosesserings-instruksjon.",
	"Attributt ''{0}'' utenfor element.",
	"Ugyldig attributt ''{0}''.",
	"Sirkul \00e6 import/include; stilark ''{0}'' er alt lest.",
	"Result-tre fragmenter kan ikke sorteres (<xsl:sort> elementer vil "+
	"bli ignorert). Du m\u00e5 sortere nodene mens du bygger treet.",
	"Formatterings-symboler ''{0}'' er alt definert.",
	"XSL versjon ''{0}'' er ikke st\u00f8ttet av XSLTC.",
	"Sirkul\00e6r variabel/parameter referanse i ''{0}''.",
	"Ugyldig operator for bin\00e6rt uttrykk.",
	"Ugyldig parameter i funksjons-kall.",
	"Andre argument til document() m\u00e5 v\00e6re et node-sett.",
	"Du m\u00e5 deklarere minst ett <xsl:when> element innenfor <xsl:choose>.",
	"Kun ett <xsl:otherwise> element kan deklareres innenfor <xsl:choose>.",
	"<xsl:otherwise> kan kun benyttes innenfor <xsl:choose>.",
	"<xsl:when> kan kun benyttes innenfor <xsl:choose>.",
	"Kun <xsl:when> og <xsl:otherwise> kan benyttes innenfor <xsl:choose>.",
	"<xsl:attribute-set> element manger 'name' attributt.",
	"Ugyldig element.",
	"''{0}'' er ikke et gyldig navn for et element.",
	"''{0}'' er ikke et gyldig navn for et attributt.",
	"Du kan ikke plassere tekst utenfor et <xsl:stylesheet> element.",
	"JAXP parser er ikke korrekt konfigurert.",
	"XSLTC-intern feil: ''{0}''",
	"St\u00f8tter ikke XSL element ''{0}''.",
	"XSLTC st\u00f8tter ikke utvidet funksjon ''{0}''.",
	"Dette dokumentet er ikke et XSL stilark "+
	"Kan ikke finne stilark ved navn ''{0}'' i dette dokumentet.",
	"Ikke implementert/gjenkjent: ''{0}''.",
	"Dokumentet inneholder ikke et XSL stilark",
	"Kan ikke tolke element ''{0}''",
	"'use'-attributtet i <xsl:key> m\u00e5 v\00e6re node, node-sett, tekst eller nummer.",
	"Det genererte XML dokumentet m\u00e5 gis versjon 1.0",
	"Ugyldig operator for relasjons-uttrykk.",
	"Finner ikke <xsl:attribute-set> element med navn ''{0}''.",
	"Kan ikke tolke attributt ''{0}''.",
	"Ukjent data-type i signatur for klassen ''{0}''.",
	"Kan ikke oversette mellom data-type ''{0}'' og ''{1}''.",

	"Dette Templates objected inneholder ingen translet klasse definisjon.",
	"Dette Templates objected inneholder ingen klasse ved navn ''{0}''.",
	"Kan ikke laste translet-klasse ''{0}''.",
	"Translet klassen er lastet man kan instansieres.",
	"ErrorListener for ''{0}'' fors\u00f8kt satt til 'null'.",
	"Kun StreamSource, SAXSource og DOMSOurce er st\u00f8ttet av XSLTC",
	"Source objekt sendt til ''{0}'' har intet innhold.",
	"Kan ikke kompilere stilark.",
	"TransformerFactory gjenkjenner ikke attributtet ''{0}''.",
	"setResult() m\u00e5 kalles f\u00f8r startDocument().",
	"Transformer objektet inneholder ikken noen translet instans.",
	"Ingen 'handler' er satt for \u00e5 ta imot generert dokument.",
	"Result objektet sendt til ''{0}'' er ikke gyldig.",
	"Fors\u00f8ker \u00e5 lese ugyldig attributt ''{0}'' fra Transformer.",
	"Kan ikke instansiere SAX2DOM adapter: ''{0}''.",
	"XSLTCSource.build() kalt uten at 'systemId' er definert.",

	"Du kan ikke bruke -i uten \u00e5 ogs\u00e5 angi klasse-navn med -o.",
	"Bruk:\n" + 
	"   xsltc [-o <klasse>] [-d <katalog>] [-j <arkiv>]\n"+
	"         [-p <pakke>] [-x] [-s] [-u] <stilark>|-i\n\n"+
	"   Der:  <klasse> er navnet du vil gi den kompilerte java klassen.\n"+
	"         <stilark> er ett eller flere XSL stilark, eller dersom -u\n"+
	"         er benyttet, en eller flere URL'er til stilark.\n"+
	"         <katalog> katalog der klasse filer vil plasseres.\n"+
	"         <arkiv> er en JAR-fil der klassene vil plasseres\n"+
	"         <pakke> er an Java 'package' klassene vil legges i.\n\n"+
	"   Annet:\n"+
	"         -i tvinger kompilatoren til \u00e5 lese fra stdin.\n"+
	"         -o ignoreres dersom flere enn ett silark kompileres.\n"+
	"         -x sl\u00e5r p\u00e5 debug meldinger.\n"+
	"         -s blokkerer alle kall til System.exit().",
	"Bruk: \n" +
	"   xslt  [-j <arkiv>] {-u <url> | <dokument>} <klasse>\n"+
	"         [<param>=<verdi> ...]\n\n" +
	"   Der:  <dokument> er XML dokumentet som skal behandles.\n" +
	"         <url> er en URL til XML dokumentet som skal behandles.\n" +
	"         <klasse> er Java klassen som skal benyttes.\n" +
	"         <arkiv> er en JAR-fil som klassen leses fra.\n"+
	"   Annet:\n"+
	"         -x sl\u00e5r p\u00e5 debug meldinger.\n"+
	"         -s blokkerer alle kall til System.exit().",

	"<xsl:sort> kan bare brukes under <xsl:for-each> eller <xsl:apply-templates>.",
	"Karaktersett ''{0}'' er ikke st\u00f8ttet av denne JVM.",
    };

    public Object handleGetObject(String key) {
	if (key == null) return null;
	if (key.equals(ErrorMsg.ERROR_MESSAGES_KEY))
	    return errorMessages;
 	else if (key.equals(ErrorMsg.COMPILER_ERROR_KEY))
	    return "Kompilator-feil: ";
	else if (key.equals(ErrorMsg.COMPILER_WARNING_KEY))
	    return "Advarsel : ";	    
 	else if (key.equals(ErrorMsg.RUNTIME_ERROR_KEY))
	    return "Kj\u00f8refeil: ";
	return(null);
    }

}
