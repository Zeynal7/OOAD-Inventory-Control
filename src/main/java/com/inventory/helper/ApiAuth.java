//package com.inventory.helper;
//
//import javax.ws.rs.core.HttpHeaders;
//import java.util.ArrayList;
//import java.util.Objects;
//import java.util.UUID;
//
//
//public class ApiAuth {
//
//    public enum AUTHENTICATION_SCHEME {
//        BEARER("Bearer");
//        private final String rawValue;
//
//        AUTHENTICATION_SCHEME(String rawValue) {
//            this.rawValue = rawValue;
//        }
//
//        public String getExtractedValue(String authHeader) {
//            return authHeader.substring(rawValue.length()).trim();
//        }
//    }
//
//
//    public static int checkAuthentication(HttpHeaders httpHeaders) throws Exception {
//        String authorizationHeader =
//                httpHeaders.getHeaderString(HttpHeaders.AUTHORIZATION);
//        String token = extractToken(authorizationHeader);
//        return checkUserAccess(token);
//    }
//
//    public static String extractToken(String authorizationHeader) throws ApiException {
//        if (authorizationHeader == null) throw new ApiException(ApiException.ExceptionCodes.NOT_AUTHORIZED);
//        return AUTHENTICATION_SCHEME.BEARER.getExtractedValue(authorizationHeader);
//    }
//
//
//    static String getToken(String userId) throws Exception{
//        String result = null;
//        try {
//            ArrayList<Object[]> queryResult = DbConnection.selectFromDB("SELECT id, token FROM user where id = ?;", new Object[]{userId});
//            result = Objects.toString(queryResult.get(0)[1], null);
//        } catch (Exception ignored) {
//        }
//
//        if (result != null) {
//            String query = "UPDATE `user` SET `last_login_date` = NOW() WHERE `user`.`id` = ?;";
//            ArrayList<Object> parameters = new ArrayList<>();
//            parameters.add(userId);
//            DbConnection.insertUpdateToDB(query, parameters, parameters.size());
//        }
//        return result;
//    }
//
//
//    // Get person id from token.
//        private static int checkUserAccess(String token) throws Exception {
//        ArrayList<Object[]> queryResult = DbConnection.selectFromDB("SELECT id, is_blocked, block_reason FROM user where token = ?;", new Object[]{token});
//
//        if(queryResult.size() > 0 && queryResult.get(0).length >= 2) {
//            if (isUserBlocked(Objects.toString(queryResult.get(0)[1], null))) {
//                ApiException apiException = new ApiException(ApiException.ExceptionCodes.BLOCKED_USER);
//                String blockReason = Objects.toString(queryResult.get(0)[2], null);
//                if (blockReason != null)
//                    apiException.setMessage("Reason for block: " + blockReason);
//                throw apiException;
//            }
//
//            int personId = Integer.parseInt(queryResult.get(0)[0].toString());
//            updateLastActiveTime(personId);
//            return personId;
//        }
//
//        throw new ApiException(ApiException.ExceptionCodes.NOT_AUTHORIZED);
//    }
//
//    private static void updateLastActiveTime(int userId) throws Exception {
//        String query = "UPDATE `user` SET `last_active_date` = NOW() WHERE `user`.`id` = ?;";
//        ArrayList<Object> parameters = new ArrayList<>();
//        parameters.add(userId);
//        DbConnection.insertUpdateToDB(query, parameters, parameters.size());
//    }
//
//    private static boolean isUserBlocked(String isBlockedStr) {
//        if (isBlockedStr != null) {
//            int isBlocked = Integer.parseInt(isBlockedStr);
//            return isBlocked != 0;
//        }
//        return false;
//    }
//
//    public static void setToken(String userId) throws Exception {
//        int loopCount = 0;
//        while (loopCount++ < 20) {
//            UUID uuid = UUID.randomUUID();
//            String token = uuid.toString();
//            try {
//                // If token is already available in db, start again.
//                try {
//                    boolean isTokenAvailable = (Objects.toString(DbConnection.selectFromDB("SELECT token FROM user where token = ?;", new Object[]{token}).get(0)[0], null) == null);
//                    if (isTokenAvailable) {
//                        continue;
//                    }
//                } catch (Exception ignored) {
//                }
//
//
//                String query = "UPDATE `user` SET `token` = ? WHERE `user`.`id` = ?;";
//                ArrayList<Object> parameters = new ArrayList<>();
//                parameters.add(token);
//                parameters.add(userId);
//                DbConnection.insertUpdateToDB(query, parameters, parameters.size());
//                return;
//            } catch (Exception ignored) {
//            }
//        }
//        throw new ApiException(ApiException.ExceptionCodes.TOKEN_INIT_PROBLEM);
//    }
//
//    static void deleteToken(String userId) {
//        try {
//            String query = "UPDATE `user` SET `token` = NULL WHERE `user`.`id` = ?;";
//            ArrayList<Object> parameters = new ArrayList<>();
//            parameters.add(userId);
//            DbConnection.insertUpdateToDB(query, parameters, parameters.size());
//        } catch (Exception ignored) {
//        }
//    }
//}