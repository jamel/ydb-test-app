package org.jamel;

import java.io.File;
import java.security.PrivateKey;

import com.yandex.ydb.auth.iam.IamAuthContext;
import com.yandex.ydb.auth.iam.Keys;
import com.yandex.ydb.core.auth.AuthProvider;
import com.yandex.ydb.core.grpc.GrpcTransport;
import com.yandex.ydb.core.rpc.RpcTransport;


/**
 * @author Sergey Polovko
 */
public class App {

    public static void main(String[] args) {
        if (args.length != 5) {
            System.err.println("Usage: java " + App.class + " <accountId> <keyId> <private_key_file_path> <endpoint> <database>");
            System.exit(1);
        }

        String accountId = args[0];
        String keyId = args[1];
        PrivateKey privateKey = Keys.privateKey(new File(args[2]));
        String endpoint = args[3];
        String database = args[4];

        IamAuthContext authContext = new IamAuthContext();
        AuthProvider authProvider = authContext.authProvider(accountId, keyId, privateKey)
            .join();

        RpcTransport transport = GrpcTransport.forEndpoint(endpoint, database)
            .withAuthProvider(authProvider)
            .build();

        try (BasicExampleV1 example = new BasicExampleV1(transport, database)) {
            example.run();
        } finally {
            authContext.close();
        }
    }
}
