export function isValidUsername(username: string): boolean {
  if (!username) return false;
  const re = /^[A-Za-z0-9_-]{3,30}$/;
  return re.test(username);
}

export function isValidEmail(email: string): boolean {
  if (!email) return false;
  const re = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;
  return re.test(email);
}

export function isUsernameOrEmail(value: string): boolean {
  return isValidUsername(value) || isValidEmail(value);
}

export function isValidPassword(password: string): boolean {
  if (!password) return false;
  // 8-12 chars, at least one uppercase, one digit and one symbol
  const re = /^(?=.{8,12}$)(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z0-9]).*$/;
  return re.test(password);
}

export function sanitizeDisplayName(input: string | null | undefined): string {
  if (!input) return '';
  // remove tags
  const stripped = input.replace(/<.*?>/g, '');
  // allow letters, numbers, spaces, underscore and hyphen
  const cleaned = stripped.replace(/[^\p{L}0-9 _-]/gu, '');
  return cleaned.trim().slice(0, 50);
}

export function containsMaliciousPayload(s: string | null | undefined): boolean {
  if (!s) return false;
  const lower = s.toLowerCase();
  if (lower.includes('<script') || lower.includes('javascript:') || lower.includes('--') || lower.includes(';drop ') || lower.includes('/*') || lower.includes('*/')) {
    return true;
  }
  return false;
}
