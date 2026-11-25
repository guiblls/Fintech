package br.com.fiap.fincontrol.service;
import java.security.*;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
public final class Senhas {
 private Senhas() {}
 private static byte[] derivar(String senha, byte[] salt) {
  PBEKeySpec spec=new PBEKeySpec(senha.toCharArray(),salt,210000,256);
  try { return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded(); }
  catch (GeneralSecurityException e) { throw new IllegalStateException("Falha ao processar senha",e); }
  finally { spec.clearPassword(); }
 }
 public static String hash(String senha) {
  byte[] salt=new byte[16];new SecureRandom().nextBytes(salt);
  return Base64.getEncoder().encodeToString(salt)+":"+Base64.getEncoder().encodeToString(derivar(senha,salt));
 }
 public static boolean confere(String senha,String hash) {
  try { String[] p=hash.split(":");return MessageDigest.isEqual(derivar(senha,Base64.getDecoder().decode(p[0])),Base64.getDecoder().decode(p[1])); }
  catch (RuntimeException e) { return false; }
 }
}
