package netzwerkverwaltung;

/**
 * ****************************************************************************
 *
 * Copyright (c) 1999-2006 AppGate Network Security AB. All Rights Reserved.
 *
 * This file contains Original Code and/or Modifications of Original Code as
 * defined in and that are subject to the MindTerm Public Source License,
 * Version 2.0, (the 'License'). You may not use this file except in compliance
 * with the License.
 *
 * You should have received a copy of the MindTerm Public Source License along
 * with this software; see the file LICENSE. If not, write to AppGate Network
 * Security AB, Otterhallegatan 2, SE-41118 Goteborg, SWEDEN
 *
 ****************************************************************************
 */
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

import java.awt.*;
import java.awt.event.*;

import com.mindbright.jca.security.SecureRandom;

import com.mindbright.ssh2.SSH2Transport;
import com.mindbright.ssh2.SSH2SimpleClient;
import com.mindbright.ssh2.SSH2ConsoleRemote;
import com.mindbright.ssh2.SSH2Preferences;
import com.mindbright.ssh2.SSH2TerminalAdapterImpl;
import com.mindbright.ssh2.SSH2StreamFilterFactory;
import com.mindbright.ssh2.SSH2StreamSniffer;
import com.mindbright.ssh2.SSH2FTPProxyFilter;
import com.mindbright.ssh2.SSH2HostKeyVerifier;

import com.mindbright.terminal.TerminalWin;
import com.mindbright.terminal.TerminalFrameTitle;
import com.mindbright.terminal.TerminalMenuHandler;
import com.mindbright.terminal.TerminalMenuHandlerFull;
import com.mindbright.terminal.TerminalMenuListener;
import com.mindbright.terminal.LineReaderTerminal;
import com.mindbright.terminal.GlobalClipboard;

import com.mindbright.gui.AWTConvenience;

import com.mindbright.util.SecureRandomAndPad;
import com.mindbright.util.RandomSeed;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Ssh2 client which opens a terminal window and asks the user where to connect
 * to. <p> Usage:
 * <code> java -cp examples.jar examples.BasicClient
 * [<em>props_file_name</em>]</code> <p> Username and password as well as server
 * can be stored in the properties file. <p> It can also read portforwards from
 * properties. Create properties named 'localN' or 'remoteN' where N is an
 * integer 0-31. The contents of the properties is in the following format:
 * <pre>
 *   [/plugin/][<em>local_host</em>:]<em>local_port</em>:<em>remote_host</em>:<em>remote_port</em>
 * </pre> This client understands the
 * <code>ftp</code> and
 * <code>sniff</code> plugins.
 *
 * @see com.mindbright.ssh2.SSH2FTPProxyFilter
 * @see com.mindbright.ssh2.SSH2StreamSniffer
 */
public final class SSHClient extends WindowAdapter
        implements TerminalMenuListener, Runnable {

    private Frame frame;
    private TerminalWin terminal;
    private SSH2Transport transport;
    private SSH2SimpleClient client;
    private SSH2ConsoleRemote console;
    private Properties props;
    private LineReaderTerminal lineReader;
    private int exitStatus;
    private String host;
    private String hostname, ports,  User,  PW;

    /**
     * Simple constructor where all required properties have good default values
     * so no properties have to be provided. However the properties is the only
     * way to change the encryption algorithms etc in this client.
     *
     * @param props SSH2 protocol properties.
     */
    public SSHClient(Properties props,String hostname, String ports, String User, String PW) {
        this.props = props;
        this.exitStatus = 1;
        this.hostname = hostname;
        this.ports = ports;
        this.User = User;
        this.PW = PW;
    }

    /**
     * Actually runs the client. This gets called from the
     * <code>main</code> function.
     */
    public void run() {
        try {
            int port;
            String user;

            /*
             * Create and show terminal window
             */
            boolean haveMenus = Boolean.valueOf(
                    props.getProperty("havemenus")).booleanValue();

            frame = (haveMenus ? AWTConvenience.newFrameWithMenuBar()
                    : new Frame());
            terminal = new TerminalWin(frame, props);
            
            RandomSeed seed = new RandomSeed();
            terminal.addAsEntropyGenerator(seed);
            Container pan = terminal.getPanelWithScrollbar();
            
            frame.setLayout(new BorderLayout());
            frame.add(pan, BorderLayout.CENTER);

            frame.setTitle("SSH Client");
            frame.addWindowListener(this);
            
            frame.pack();
            frame.show();

            /*
             * Prompt for where to connect to
             */
            lineReader = new LineReaderTerminal(terminal);
            if (hostname.equals("")) {
                host = props.getProperty("server");
                port = getPort(props.getProperty("port"));
                user = props.getProperty("username");
            } else {
                host = hostname;
                port = getPort(ports);
                user = User;
            }
            terminal.write("Basic SSH2 client \n\r");

            while (host == null) {
                host = lineReader.promptLine("\r\nssh2 server[:port]: ",
                        null, false);
            }

            int i = host.indexOf(':');
            if (i != -1) {
                port = getPort(host.substring(i + 1));
                host = host.substring(0, i);
            }

            while (user == null) {
                user = lineReader.promptLine(host + " login: ", null, false);
            }

            // Create the preferences object
            SSH2Preferences prefs = new SSH2Preferences(props);

            SecureRandomAndPad secureRandom = new SecureRandomAndPad(new SecureRandom(seed.getBytesBlocking(20, false)));

            transport = new SSH2Transport(new Socket(host, port), prefs, secureRandom);


            String fingerprint = props.getProperty("fingerprint." + host + "." + port);


            if (fingerprint != null) {
                transport.setEventHandler(new SSH2HostKeyVerifier(fingerprint));
            }

            client = null;


            String auth = props.getProperty("auth-method");
            if ("publickey".equals(auth)) {
                String keyFile = props.getProperty("private-key");
                String keyPasswd = props.getProperty("passphrase");
                client = new SSH2SimpleClient(transport, user, keyFile,
                        keyPasswd);
            } else {
                String passwd = "";
                if (PW.equals("")) {
                    passwd = props.getProperty("password");
                    while (passwd == null) {
                        passwd = lineReader.promptLine(
                                user + "@" + host + "'s password: ", null, true);
                    }
                } else {
                    passwd = PW;
                }
                client = new SSH2SimpleClient(transport, user, passwd);
            }


            lineReader.detach();


            startForwards();

            console = new SSH2ConsoleRemote(client.getConnection());
            SSH2TerminalAdapterImpl termAdapter = new SSH2TerminalAdapterImpl(terminal);
            
            if (!console.terminal(termAdapter)) {
                throw new Exception("Couldn't start terminal!");
            }


            exitStatus = console.waitForExitStatus();

        } catch (LineReaderTerminal.ExternalMessageException e) {
            // ignore
        } catch (Exception e) {
            JOptionPane.showMessageDialog(new JFrame(), e);
            
        } finally {
            if (frame != null) {
                frame.dispose();
            }
        }
    }

    /**
     * Get the exit status from the SSH2ConsoleRemote instance
     *
     * @return the exit status
     */
    public int getExitStatus() {
        return exitStatus;
    }

    /**
     * Starts any portforwards specified in the properties.
     */
    private void startForwards() {
        int i;
        for (i = 0; i < 32; i++) {
            String spec = props.getProperty("local" + i);
            if (spec == null) {
                break;
            }
            Object[] components = parseForwardSpec(spec, "127.0.0.1");
            try {
                SSH2StreamFilterFactory filter = null;
                if ("ftp".equals(components[0])) {
                    filter = new SSH2FTPProxyFilter((String) components[1],
                            host);
                } else if ("sniff".equals(components[0])) {
                    filter = SSH2StreamSniffer.getFilterFactory();
                }
                client.getConnection().
                        newLocalForward((String) components[1],
                        ((Integer) components[2]).intValue(),
                        (String) components[3],
                        ((Integer) components[4]).intValue(),
                        filter);
                terminal.write("started local forward: " + spec + "\n\r");
            } catch (IOException e) {
                terminal.write("failed local forward: " + spec
                        + e.getMessage() + "\n\r");
            }
        }
        for (i = 0; i < 32; i++) {
            String spec = props.getProperty("remote" + i);
            if (spec == null) {
                break;
            }
            Object[] components = parseForwardSpec(spec, "127.0.0.1");
            client.getConnection().newRemoteForward((String) components[1],
                    ((Integer) components[2]).intValue(),
                    (String) components[3],
                    ((Integer) components[4]).intValue());
            terminal.write("started remote forward: " + spec + "\n\r");
        }
    }

    /**
     * Parse a portforward specification string
     */
    public static Object[] parseForwardSpec(String spec, String listenAddr)
            throws IllegalArgumentException {
        int d1, d2, d3;
        String tmp;
        Object[] components = new Object[5];

        if (spec.startsWith("/")) {
            int i = spec.indexOf('/', 1);
            components[0] = spec.substring(1, i);
            spec = spec.substring(i + 1);
        } else {
            components[0] = "general";
        }

        d1 = spec.indexOf(':');
        d2 = spec.lastIndexOf(':');
        if (d1 == d2) {
            throw new IllegalArgumentException("Invalid port forward spec. "
                    + spec);
        }

        d3 = spec.indexOf(':', d1 + 1);

        if (d3 != d2) {
            components[1] = spec.substring(0, d1);
            components[2] = Integer.valueOf(spec.substring(d1 + 1, d3));
            components[3] = spec.substring(d3 + 1, d2);
        } else {
            components[1] = listenAddr;
            components[2] = Integer.valueOf(spec.substring(0, d1));
            components[3] = spec.substring(d1 + 1, d2);
        }

        tmp = spec.substring(d2 + 1);
        components[4] = Integer.valueOf(tmp);

        return components;
    }

    /**
     * Get the port number of the ssh server stored in the string. Defaults to
     * 22, the ssh standard port, if none is specified.
     */
    private static int getPort(String port) {
        int p;
        try {
            p = Integer.parseInt(port);
        } catch (Exception e) {
            p = 22;
        }
        return p;
    }

    private String getTitle() {
        return "Basic SSH2 Client";
    }

    /**
     * Close the connection to the server (if any) in a controlled way.
     */
    public void doClose() {
        if (lineReader != null) {
            lineReader.breakPromptLine("");
        }
        if (console != null) {
            console.close();
        }
        if (transport != null) {
            transport.normalDisconnect("User disconnects");
        }
    }

    /**
     * Overide corresponding function in java.awt.event.WindowAdapter
     */
    public void windowClosing(WindowEvent e) {
        doClose();
    }

    /*
     * TerminalMenuListener interface implementation
     */
    public void close(TerminalMenuHandler origMenu) {
        doClose();
    }

    public void update() {
        // Ignore
    }

   
}
