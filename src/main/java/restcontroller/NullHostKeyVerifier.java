/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restcontroller;

import java.security.PublicKey;
import net.schmizz.sshj.transport.verification.HostKeyVerifier;

/**
 *
 * @author Alex
 */
class NullHostKeyVerifier implements HostKeyVerifier {

    @Override
    public boolean verify(String arg0, int arg1, PublicKey arg2) {
        return true;
    }

}
