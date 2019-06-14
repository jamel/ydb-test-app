package org.jamel;

import java.io.File;
import java.security.PrivateKey;

import com.yandex.ydb.auth.iam.IamAuthContext;
import com.yandex.ydb.auth.iam.Keys;
import com.yandex.ydb.core.auth.AuthProvider;
import com.yandex.ydb.core.grpc.GrpcTransport;
import com.yandex.ydb.core.grpc.GrpcTransportBuilder;
import com.yandex.ydb.table.Session;
import com.yandex.ydb.table.TableClient;
import com.yandex.ydb.table.query.DataQueryResult;
import com.yandex.ydb.table.result.ResultSetReader;
import com.yandex.ydb.table.result.ValueReader;
import com.yandex.ydb.table.rpc.grpc.GrpcTableRpc;
import com.yandex.ydb.table.transaction.TxControl;


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

        GrpcTransport transport = GrpcTransportBuilder.forEndpoint(endpoint, database)
            .withAuthProvider(authProvider)
            .build();

        TableClient tableClient = TableClient.newClient(GrpcTableRpc.ownTransport(transport))
            .build();

        try {
            Session session = tableClient.createSession()
                .join()
                .expect("cannot create session");

            try {
                DataQueryResult result = session.executeDataQuery("select 1;", TxControl.onlineRo())
                    .join()
                    .expect("cannot execute query");

                ResultSetReader resultSet = result.getResultSet(0);
                while (resultSet.next()) {
                    ValueReader column = resultSet.getColumn(0);
                    System.out.println(column.toString());
                }
            } finally {
                session.close()
                    .join()
                    .expect("cannot close session");
            }
        } finally {
            tableClient.close();
            authContext.close();
        }
    }
}
