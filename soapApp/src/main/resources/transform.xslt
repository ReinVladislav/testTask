<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="xml" indent="yes" encoding="UTF-8"/>

    <!-- Match the root element 'person' -->
    <xsl:template match="/person">
        <xsl:element name="person">
            <xsl:attribute name="name">
                <xsl:value-of select="name"/>
            </xsl:attribute>
            <xsl:attribute name="surname">
                <xsl:value-of select="surname"/>
            </xsl:attribute>
            <xsl:attribute name="patronymic">
                <xsl:value-of select="patronymic"/>
            </xsl:attribute>
            <xsl:attribute name="birthDate">
                <xsl:value-of select="birthDate"/>
            </xsl:attribute>
            <xsl:attribute name="gender">
                <xsl:value-of select="gender"/>
            </xsl:attribute>
            <xsl:element name="document">
                <xsl:attribute name="series">
                    <xsl:value-of select="document/series"/>
                </xsl:attribute>
                <xsl:attribute name="number">
                    <xsl:value-of select="document/number"/>
                </xsl:attribute>
                <xsl:attribute name="type">
                    <xsl:value-of select="document/type"/>
                </xsl:attribute>
                <xsl:attribute name="issueDate">
                    <xsl:value-of select="document/issueDate"/>
                </xsl:attribute>
            </xsl:element>
        </xsl:element>
    </xsl:template>
</xsl:stylesheet>
