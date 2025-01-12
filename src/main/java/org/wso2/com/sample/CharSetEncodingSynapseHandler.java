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

        if (log.isDebugEnabled()) {
            log.debug("Starting handleRequestInFlow for message context: " + axis2context.getMessageID());
        }

        /*
         * Check if the character set encoding (CHARACTER_SET_ENCODING) is specified
         * in the Axis2 message context. If the character set encoding is present in the Content-Type header,
         * it should be stored in the Axis2 message context under the CHARACTER_SET_ENCODING property.
         */
        String charSetEnc = (String) axis2context.getProperty(Constants.Configuration.CHARACTER_SET_ENCODING);



        /*
         * If CHARACTER_SET_ENCODING is not found in the message context,
         * attempt to retrieve it from the operation context.
         */
        if (charSetEnc == null) {
            if (log.isDebugEnabled()) {
                log.debug("CHARACTER_SET_ENCODING not found in the Axis2 Message Context");
            }
            OperationContext opctx = axis2context.getOperationContext();
            if (opctx != null) {
                charSetEnc = (String) opctx.getProperty(Constants.Configuration.CHARACTER_SET_ENCODING);
                if (log.isDebugEnabled()) {
                    log.debug("CHARACTER_SET_ENCODING retrieved from operation context: " + charSetEnc);
                }
            } else if (log.isDebugEnabled()) {
                log.debug("Operation context is null. Unable to retrieve CHARACTER_SET_ENCODING from it.");
            }

            /*
             * If CHARACTER_SET_ENCODING is still not found in the operation context,
             * try to retrieve it from the XML_DECLARATION_ENCODING property.
             * This property is set by the application/xml builder retrieving the encoding
             * defined in the XML declaration of the incoming message payload.
             */
            if (charSetEnc == null) {
                charSetEnc = (String) axis2context.getProperty(Constants.Configuration.XML_DECLARATION_ENCODING);
                if (log.isDebugEnabled()) {
                    log.debug("CHARACTER_SET_ENCODING retrieved from XML_DECLARATION_ENCODING: " + charSetEnc);
                }
            }

            /*
             * If no character set encoding is found, use the default encoding
             * specified by Axis2 (DEFAULT_CHAR_SET_ENCODING).
             */
            if (charSetEnc == null) {
                charSetEnc = org.apache.axis2.context.MessageContext.DEFAULT_CHAR_SET_ENCODING;
                if (log.isDebugEnabled()) {
                    log.debug("CHARACTER_SET_ENCODING not found. Using default encoding: " + charSetEnc);
                }
            }

            // Set the determined character set encoding in the Axis2 message context.
            axis2context.setProperty(Constants.Configuration.CHARACTER_SET_ENCODING, charSetEnc);
            if (log.isDebugEnabled()) {
                log.debug("CHARACTER_SET_ENCODING set in Axis2 message context: " + charSetEnc);
            }
        } else if (log.isDebugEnabled()) {
            log.debug("CHARACTER_SET_ENCODING already present in Axis2 message context: " + charSetEnc);
        }

        if (log.isDebugEnabled()) {
            log.debug("Completed handleRequestInFlow for message context: " + axis2context.getMessageID());
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