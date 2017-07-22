
package org.interpss.grg;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * Grid Research for Good (GRG) data schema v.1.0. Copyright (c) 2017 The Grid Research for Good (GRG) Team (James Anderson, Russell Bent, Daniel Bienstock, Carleton Coffrin, Ferdinando Fioretto, Ian Hiskens, Steven Low, Patrick Panciatici, Pascal Van Hentenryck). Disclaimer: The following schema is subject to changes and revisions. Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated. documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions: The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "grg_version",
    "description",
    "network",
    "mappings",
    "market",
    "units"
})
public class GRGSchema {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("grg_version")
    private String grgVersion;
    @JsonProperty("description")
    private String description;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("network")
    private Network network;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("mappings")
    private Mappings mappings;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("market")
    private Market market;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("units")
    private Units units;

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("grg_version")
    public String getGrgVersion() {
        return grgVersion;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("grg_version")
    public void setGrgVersion(String grgVersion) {
        this.grgVersion = grgVersion;
    }

    public GRGSchema withGrgVersion(String grgVersion) {
        this.grgVersion = grgVersion;
        return this;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    public GRGSchema withDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("network")
    public Network getNetwork() {
        return network;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("network")
    public void setNetwork(Network network) {
        this.network = network;
    }

    public GRGSchema withNetwork(Network network) {
        this.network = network;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("mappings")
    public Mappings getMappings() {
        return mappings;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("mappings")
    public void setMappings(Mappings mappings) {
        this.mappings = mappings;
    }

    public GRGSchema withMappings(Mappings mappings) {
        this.mappings = mappings;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("market")
    public Market getMarket() {
        return market;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("market")
    public void setMarket(Market market) {
        this.market = market;
    }

    public GRGSchema withMarket(Market market) {
        this.market = market;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("units")
    public Units getUnits() {
        return units;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("units")
    public void setUnits(Units units) {
        this.units = units;
    }

    public GRGSchema withUnits(Units units) {
        this.units = units;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(grgVersion).append(description).append(network).append(mappings).append(market).append(units).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof GRGSchema) == false) {
            return false;
        }
        GRGSchema rhs = ((GRGSchema) other);
        return new EqualsBuilder().append(grgVersion, rhs.grgVersion).append(description, rhs.description).append(network, rhs.network).append(mappings, rhs.mappings).append(market, rhs.market).append(units, rhs.units).isEquals();
    }

}
