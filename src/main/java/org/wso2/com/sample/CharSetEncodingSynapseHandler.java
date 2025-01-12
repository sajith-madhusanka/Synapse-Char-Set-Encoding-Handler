package org.wso2.com.sample;

import org.apache.axis2.Constants;
import org.apache.axis2.context.OperationContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.AbstractSynapseHandler;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;

public class CharSetEncodingSynapseHandler extends AbstractSynapseHandler {
    private static final Log log = LogFactory.getLog(CharSetEncodingSynapseHandler.class);

    // Handle incoming message flow to the EI server
    public boolean handleRequestInFlow(MessageContext context) {

        org.apache.axis2.context.MessageContext axis2context =
                ((Axis2MessageContext) context).getAxis2MessageContext();

        /*
         * Check if the character set encoding (CHARACTER_SET_ENCODING) is specified
         * in the Axis2 message context. If the character set encoding is present in the Content-Type header,
         * it should be stored in the Axis2 message context under the CHARACTER_SET_ENCODING property.
         */
        String charSetEnc = (String) axis2context
                .getProperty(Constants.Configuration.CHARACTER_SET_ENCODING);

        /*
         * If CHARACTER_SET_ENCODING is not found in the message context,
         * attempt to retrieve it from the operation context.
         */
        if (charSetEnc == null) {
            OperationContext opctx = axis2context.getOperationContext();
            if (opctx != null) {
                charSetEnc = (String) opctx
                        .getProperty(Constants.Configuration.CHARACTER_SET_ENCODING);
            }

            /*
             * If CHARACTER_SET_ENCODING is still not found in the operation context,
             * try to retrieve it from the XML_DECLARATION_ENCODING property.
             * This property is set by the application/xml builder retrieving the encoding
             * defined in the XML declaration of the incoming message payload.
             */
            if (charSetEnc == null) {
                charSetEnc = (String) axis2context.getProperty(Constants.Configuration.XML_DECLARATION_ENCODING);
            }

            /*
             * If no character set encoding is found, use the default encoding
             * specified by Axis2 (DEFAULT_CHAR_SET_ENCODING).
             */
            if (charSetEnc == null) {
                charSetEnc = org.apache.axis2.context.MessageContext.DEFAULT_CHAR_SET_ENCODING;
            }

            // Set the determined character set encoding in the Axis2 message context.
            axis2context.setProperty(Constants.Configuration.CHARACTER_SET_ENCODING, charSetEnc);
        }

        return true;
    }

    @Override
    public boolean handleRequestOutFlow(MessageContext messageContext) {
        return true;
    }

    @Override
    public boolean handleResponseInFlow(MessageContext messageContext) {
        return true;
    }

    @Override
    public boolean handleResponseOutFlow(MessageContext messageContext) {
        return true;
    }

}