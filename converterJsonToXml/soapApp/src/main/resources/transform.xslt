<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:tns="http://soapapp.com/soapapp">

    <xsl:output method="xml" indent="yes"/>

    <xsl:template match="tns:getPersonRequest">
        <response>
            <person>
                <!-- Attributes without namespace prefix -->
                <xsl:attribute name="name">
                    <xsl:value-of select="person/name"/>
                </xsl:attribute>
                <xsl:attribute name="surname">
                    <xsl:value-of select="person/surname"/>
                </xsl:attribute>
                <xsl:attribute name="patronymic">
                    <xsl:value-of select="person/patronymic"/>
                </xsl:attribute>
                <xsl:attribute name="birthDate">
                    <xsl:value-of select="person/birthDate"/>
                </xsl:attribute>
                <xsl:attribute name="gender">
                    <xsl:value-of select="person/gender"/>
                </xsl:attribute>
                <!-- Document element with attributes -->
                <document>
                    <xsl:attribute name="series">
                        <xsl:value-of select="person/document/series"/>
                    </xsl:attribute>
                    <xsl:attribute name="number">
                        <xsl:value-of select="person/document/number"/>
                    </xsl:attribute>
                    <xsl:attribute name="type">
                        <xsl:value-of select="person/document/type"/>
                    </xsl:attribute>
                    <xsl:attribute name="issueDate">
                        <xsl:value-of select="person/document/issueDate"/>
                    </xsl:attribute>
                </document>
            </person>
        </response>

    </xsl:template>

</xsl:stylesheet>
